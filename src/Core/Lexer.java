package Core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

// 0) Maven or Gradle
// 1) CodeStyle (Google)
// 2) Lombok (Annotations -> getters/setters etc.)
// 3) SonarLint plugin
// 4) Java8+ (lambda (functional interfaces), streams)

// Optional:
// 1) DI -> Spring



public abstract class Lexer {

    private static Logger Lexical_analysis = Logger.getLogger(Lexer.class.getName());


    public static List<Operation> brainFuckToLex(String code) {
        //Создаем массив лексем (которые уже являются опкодами и готовы к исполнению)
        List<Operation> retValue = new ArrayList<>();
        int pos = 0;

        //Приходимся по всем символам
        while (pos < code.length()) {
            switch (code.charAt(pos++)) {
                //Как и говорилось ранее, некоторые команды эквивалентны
                case '>':
                    retValue.add(new Operation(Operation.Type.SHIFT, +1));
                    break;
                case '<':
                    retValue.add(new Operation(Operation.Type.SHIFT, -1));
                    break;

                case '+':
                    retValue.add(new Operation(Operation.Type.ADD, +1));
                    break;
                case '-':
                    retValue.add(new Operation(Operation.Type.ADD, -1));
                    break;

                case '.':
                    retValue.add(new Operation(Operation.Type.OUT));
                    break;
                case ',':
                    retValue.add(new Operation(Operation.Type.IN));
                    break;
                case '[':
                    char next = code.charAt(pos);

                    //проверяем, является ли это обнулением ячейки ([+] или [-])
                    if ((next == '+' || next == '-') && code.charAt(pos + 1) == ']') {
                        retValue.add(new Operation(Operation.Type.ZERO));
                        pos += 2;
                    } else
                        retValue.add(new Operation(Operation.Type.BEGIN_WHILE));
                    break;
                case ']':
                    retValue.add(new Operation(Operation.Type.END_WHILE));
                    break;
            }
        }
        //Lexical_analysis.info("Logger Name: "+Lexical_analysis.getName());
        Lexical_analysis.info("|The end of the analysis|");
        return retValue;
    }

    public static void printOperation(Operation x) {
        System.out.println(x.type + " " + x.arg);
    }
    public static void outer(List<Operation> retValue) {
        for (Operation x : retValue) {
           printOperation(x);
        }
    }

    public static String LexToBrainFuck(List<Operation> retValue) {
        StringBuilder BFcode = new StringBuilder();
        for (Operation x : retValue) {
            switch (x.type) {
                case SHIFT: {
                    if (x.arg > 0)
                        BFcode.append('>');
                    else
                        BFcode.append('<');
                    break;
                }
                case ADD: {
                    if (x.arg > 0)
                        BFcode.append('+');
                    else
                        BFcode.append('-');
                    break;
                }
                case IN: {
                    BFcode.append(',');
                    break;
                }
                case OUT: {
                    BFcode.append('.');
                    break;
                }
                case ZERO: {
                    BFcode.append("[-]");
                    break;
                }
                case BEGIN_WHILE: {
                    BFcode.append('[');
                    break;
                }
                case END_WHILE: {
                    BFcode.append(']');
                    break;
                }
            }
        }
        return BFcode.toString();
    }
}