public @interface MyAnnotation {
  String arg() default "";
  String[] args() default "";
  String argNoDefault;
  String[] argsNoDefault;
}