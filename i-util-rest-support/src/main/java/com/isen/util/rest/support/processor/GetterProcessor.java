package com.isen.util.rest.support.processor;

import com.isen.util.rest.support.annotation.Getter;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @author Isen
 * @date 2018/12/20 17:36
 * @since 1.0
 */
@SupportedAnnotationTypes("com.isen.util.rest.support.annotation.Getter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GetterProcessor extends AbstractProcessor{

    /**
     * 主要是用来在编译期打log用的
     */
    private Messager messager;

    /**
     * 提供了待处理的抽象语法树
     */
    private JavacTrees trees;

    /**
     * 封装了创建AST节点的一些方法
     */
    private TreeMaker treeMaker;

    /**
     * 提供了创建标识符的方法
     */
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //利用roundEnv的getElementsAnnotatedWith方法过滤出被Getter这个注解标记的类，并存入set
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Getter.class);
        //遍历这个set里的每一个元素，并生成jCTree这个语法树
        set.forEach(element -> {
            JCTree jcTree = trees.getTree(element);
            //创建一个TreeTranslator，并重写其中的visitClassDef方法，这个方法处理遍历语法树得到的类定义部分jcClassDecl
            jcTree.accept(new TreeTranslator() {
                //利用TreeTranslator去处理jcTree
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    //创建一个jcVariableDeclList保存类的成员变量
                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();

                    //遍历jcTree的所有成员(包括成员变量和成员函数和构造函数)，过滤出其中的成员变量，并添加进jcVariableDeclList
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                        }
                    }

                    //将jcVariableDeclList的所有变量转换成需要添加的getter方法，并添加进jcClassDecl的成员中
                    jcVariableDeclList.forEach(jcVariableDecl -> {
                        messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
                    });

                    //调用默认的遍历方法遍历处理后的jcClassDecl
                    super.visitClassDef(jcClassDecl);
                }

            });
        });

        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        //逻辑就是读取变量的定义，并创建对应的Getter方法，并试图用驼峰命名法。

        //首先，messager的printMessage方法在打印log的时候会自动过滤重复的log信息。
        //其次，这里的list并不是java.util里面的list，而是一个自定义的list，这个list的用法比较坑爹，他采用的是这样的方式

        //挺有趣的，用这种叫cons而不是list的数据结构，添加元素的时候就把自己赋给自己的tail,新来的元素放进head。不过需要注意的是这个东西不支持链式调用，prepend之后还要将新值赋给自己。
        //而且这里在创建getter方法的时候还要把参数写全写对了，尤其是添加this指针的这种用法。

        ListBuffer<JCStatement> statements = new ListBuffer<>();
        statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()), jcVariableDecl.vartype, List.nil(), List.nil(), List.nil(), body, null);
    }

    private Name getNewMethodName(Name name) {
        String s = name.toString();
        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }
}
