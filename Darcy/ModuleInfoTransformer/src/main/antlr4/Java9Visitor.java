package main.antlr4;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.util.Pair;

public class Java9Visitor extends Java9BaseListener
{
    private ArrayList<Pair<String,String>> excessDirectives;
    private ArrayList<String> importList;
    private String transformedModuleInfo;

    public Java9Visitor(ArrayList<Pair<String,String>> excessDirectives) throws IOException{
        this.excessDirectives = excessDirectives;
        transformedModuleInfo = "";
        importList = new ArrayList<String>();
    }
    public static boolean skip_print = false;
    public static boolean skip_module = true;
    public static boolean transitive = false;

    @Override public void visitTerminal(TerminalNode node) {
        if(skip_module == false) {
            if (skip_print == false) {
                if(transitive){
                    System.out.print(node.getSymbol().getText().replace("transitive","") + " ");
                    transformedModuleInfo += node.getSymbol().getText().replace("transitive","") + " ";
                }else {
                    System.out.print(node.getSymbol().getText() + " ");
                    transformedModuleInfo += node.getSymbol().getText() + " ";
                }

                if (node.getSymbol().getText().equals(";") || node.getSymbol().getText().equals("{") || node.getSymbol().getText().equals("}")) {
                    System.out.println();
                    transformedModuleInfo += "\n";
                }
            }
        }
    }

    @Override
    public void enterImportDeclaration(Java9Parser.ImportDeclarationContext ctx){
        importList.add(ctx.getText().replace("import","import "));
    }

    //    @Override
    public void enterModuleDeclaration(Java9Parser.ModuleDeclarationContext ctx){
        skip_module = true;
        for(Pair<String,String> excDir : excessDirectives){
            if(ctx.moduleName().getText().equals(excDir.getKey().split(":")[0])) { //check module name
                skip_module = false;
                if(importList.size()>0){
                    for(String importItem : importList) {
                        transformedModuleInfo += importItem + "\n";
                    }
                }
            }
        }
    }
    @Override
    public void enterModuleDirective(Java9Parser.ModuleDirectiveContext ctx) {
        if(skip_module==false) {


            for (Pair<String, String> excDir : excessDirectives) {

//                //debug
//                if(ctx.getParent().getText().substring(6, ctx.getParent().getText().indexOf("{")).equals("pl.azarnow.vstreamer.app"))
//                    if(ctx.getText().startsWith("uses"))
//                        System.out.println("***********"+ctx.getText());

                if (ctx.getParent().getText().substring(6, ctx.getParent().getText().indexOf("{")).equals(excDir.getKey().split(":")[0])) { //check module name


                    if(excDir.getKey().split(":")[1].equals("opensto")){
                        if (ctx.getChild(0).getText().equals("opens")){
                            if (ctx.getText().substring(5).equals(excDir.getValue().replaceAll("\\[| |]","") + ";")) { //check directive module name
                                skip_print = true;
                            }
                            if(ctx.getText().contains("javafx.base"))
                                skip_print = true;
                        }
                    }else if((excDir.getKey().split(":")[1]).equals("exportsto")){
                        if (ctx.getChild(0).getText().equals("exports")){
                            //data main, main data
                            // bugggg
                            if (ctx.getText().substring(7).equals(excDir.getValue().replaceAll("\\[| |]","") + ";")) { //check directive module name
                                skip_print = true;
                            }
                        }
                    }else if((excDir.getKey().split(":")[1]).equals("requirestransitive")){
                        if (ctx.getChild(0).getText().equals("requires") && ctx.getText().contains("requirestransitive")){ //check direcitve
                            if (ctx.getText().substring("requirestransitive".length()).equals(excDir.getValue() + ";")) { //check directive module name
                                transitive = true;
                                skip_print = false;
                            }
                        }


                    }else if((excDir.getKey().split(":")[1]).equals("uses")){
                        if (ctx.getText().substring(excDir.getKey().split(":")[1].length()).equals(excDir.getValue() + ";")
                                || excDir.getValue().contains(ctx.getText().substring(excDir.getKey().split(":")[1].length(),ctx.getText().length()-1)) ) { //check directive module name
                            skip_print = true;
                        }

                    }else if((excDir.getKey().split(":")[1]).equals("provides")){
                        if (ctx.getText().contains("provides")){
                            if(ctx.getText().substring(excDir.getKey().split(":")[1].length(),ctx.getText().indexOf("with")).equals(excDir.getValue()) ) {
                                skip_print = true;
                            }
                            if(excDir.getValue().contains("java.lang.System")){
                                if(ctx.getText().substring(excDir.getKey().split(":")[1].length(),ctx.getText().indexOf("with")).contains(excDir.getValue().substring(17)))
                                    skip_print = true;
                            }
                        }


                    }else if (ctx.getChild(0).getText().equals(excDir.getKey().split(":")[1])){ //check directive

                       if (ctx.getText().substring(excDir.getKey().split(":")[1].length()).equals(excDir.getValue() + ";")) { //check directive module name
                                skip_print = true;
                        } else if(ctx.getText().startsWith("requirestransitive") && ctx.getText().substring("requirestransitive".length()).equals(excDir.getValue() + ";")){
                                skip_print = true;
                        }
                    }
                }
            }
        }else{
            skip_print = true;
        }

    }
    @Override
    public void exitModuleDirective(Java9Parser.ModuleDirectiveContext ctx) {
        skip_print = false;
        transitive = false;
    }

    @Override
    public void exitModuleDeclaration(Java9Parser.ModuleDeclarationContext ctx) {
        skip_module = true;
        importList.clear();
    }

    public String getTransformedModuleInfo() {
        return transformedModuleInfo;
    }
}