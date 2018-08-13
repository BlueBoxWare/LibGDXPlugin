import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

@GDXTag("kotlin1", "kotlin2", "kotlin3")
class TaggedKotlinClass1

@GDXTag("kotlin1", "kotl<caret>in2", "kotlin3")
class TaggedKotlinClass2

@GDXTag("kotlin1", "kotlin2", "kotlin3")
class TaggedKotlinClass3