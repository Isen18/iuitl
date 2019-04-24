package com.isen.util.rest.support.test;
//
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.TypeSpec;

/**
 * @author Isen
 * @date 2018/12/26 11:52
 * @since 1.0
 */
public class MainClass {

    public static void main(String[] args) {
        MainClass mainClass = new MainClass();
//        mainClass.generateHelloWord();
    }

//    private void generateHelloWord() {
//        MethodSpec main = MethodSpec.methodBuilder("show")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .addStatement("$T.out.println($S)", System.class, "Hello World!")
//                .build();
//        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
//                .addModifiers(Modifier.PUBLIC)
//                .addMethod(main)
//                .build();
//
//        JavaFile javaFile = JavaFile.builder("com.isen", helloWorld).build();
//        File outputFile = new File("src/");
//
//        try {
//            javaFile.writeTo(outputFile);
//            javaFile.writeTo(System.out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
