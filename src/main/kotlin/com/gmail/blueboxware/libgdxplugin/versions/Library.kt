package com.gmail.blueboxware.libgdxplugin.versions

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.io.HttpRequests
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.w3c.dom.DOMException
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.FactoryConfigurationError
import javax.xml.parsers.ParserConfigurationException

/*
 * Copyright 2017 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
internal open class Library(
        val name: String,
        val groupId: String,
        val artifactId: String,
        private val repository: Repository = Repository.MAVEN_CENTRAL,
        val extKeys: List<String>? = null
) {

  private var latestVersion: MavenComparableVersion? = null

  var lastUpdated: Long = 0
    protected set

  val versionKey = PERSISTENT_STATE_KEY_VERSION_PREFIX + groupId + "_" + artifactId
  private val timeKey = PERSISTENT_STATE_KEY_TIME_PREFIX + groupId + "_" + artifactId

  open fun getLatestVersion(versionManager: VersionManager) = latestVersion

  open fun updateLatestVersion(versionManager: VersionManager, networkAllowed: Boolean): Boolean {

    val currentTime = System.currentTimeMillis()

    if (latestVersion != null &&  currentTime - lastUpdated < VersionManager.SCHEDULED_UPDATE_INTERVAL * 2) {
      return false
    }

    PropertiesComponent.getInstance()?.let { propertiesComponent ->
      propertiesComponent.getValue(versionKey)?.let { version ->
        propertiesComponent.getValue(timeKey)?.toLongOrNull()?.let { time ->
          if (lastUpdated < time || latestVersion == null) {
            lastUpdated = time
            latestVersion = MavenComparableVersion(version)
            if (currentTime - lastUpdated < VersionManager.SCHEDULED_UPDATE_INTERVAL) {
              return false
            }
          } else if (latestVersion != null) {
            propertiesComponent.setValue(versionKey, latestVersion.toString())
            propertiesComponent.setValue(timeKey, lastUpdated.toString())
          }
        }
      }
    }

    if (currentTime - lastUpdated > VersionManager.SCHEDULED_UPDATE_INTERVAL && networkAllowed) {

      fetchVersions(
              onSuccess = { versions ->
                latestVersion = versions.map(::MavenComparableVersion).max()
                lastUpdated = System.currentTimeMillis()
                PropertiesComponent.getInstance()?.let { propertiesComponent ->
                  propertiesComponent.setValue(versionKey, latestVersion.toString())
                  propertiesComponent.setValue(timeKey, lastUpdated.toString())
                }
              },
              onFailure = {
                lastUpdated = System.currentTimeMillis()
              }
      )

      return true
    }

    return false

  }

  protected fun fetchVersions(onSuccess: (List<String>) -> Unit, onFailure: () -> Unit) {

    val url = (TEST_URL ?: repository.baseUrl) + groupId.replace('.', '/') + "/" + artifactId + "/" + META_DATA_FILE

    VersionManager.LOG.info("Fetching $url")

    ApplicationManager.getApplication().executeOnPooledThread {

      try {

        HttpRequests.request(url).connect { request ->

          try {
            extractVersionsFromMavenMetaData(request.inputStream)?.let { versions ->
              onSuccess(versions)
            }
          } catch (e: IOException) {
            VersionManager.LOG.warn("Could not fetch $url", e)
            onFailure()
          }

        }

      } catch (e: IOException) {
        VersionManager.LOG.warn("Could not fetch $url", e)
        onFailure()
      }

    }

  }

  companion object {

    var TEST_URL: String? = null

    const val META_DATA_FILE = "maven-metadata.xml"

    const val PERSISTENT_STATE_KEY_VERSION_PREFIX = "com.gmail.blueboxware.libgdxplugin.versions."
    const val PERSISTENT_STATE_KEY_TIME_PREFIX = "com.gmail.blueboxware.libgdxplugin.time."

    fun extractVersionsFromMavenMetaData(inputStream: InputStream): List<String>? {

      try {

        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = builder.parse(inputStream)
        val versionElements = (document.getElementsByTagName("versioning").item(0) as? Element)?.getElementsByTagName("version") ?: return null

        val result = mutableListOf<String>()

        for (index in 0 until versionElements.length) {
          val content = (versionElements.item(index) as? Element)?.textContent ?: continue
          result.add(content)
        }

        return result

      } catch (e: Exception) {
        if (e is FactoryConfigurationError || e is ParserConfigurationException || e is IOException || e is SAXException || e is IllegalArgumentException || e is DOMException) {
          VersionManager.LOG.info(e)
          return null
        } else {
          throw e
        }
      }

    }

  }

}

