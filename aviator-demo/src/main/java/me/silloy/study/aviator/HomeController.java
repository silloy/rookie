package me.silloy.study.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.io.IOException;

/**
 * @author shaohuasu
 * @since 1.8
 */
public class HomeController {

    public static void main(String[] args) throws Exception {
        // Compile the script into a Expression instance.
        Expression exp = AviatorEvaluator.getInstance().compileScript("aviator-demo/scripts/hello.av");
        // Run the exprssion.
        exp.execute();
    }

}
