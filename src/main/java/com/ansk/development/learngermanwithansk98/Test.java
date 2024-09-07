package com.ansk.development.learngermanwithansk98;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String text = "Mein Lieblingsbuch ist „Der kleine Prinz“ von Antoine de Saint-Exupéry. Ich lese dieses Buch, **weil** es **sehr poetisch ist** und **tiefe Gedanken** über das Leben und die Menschheit enthält. Die Geschichte handelt von **einem** kleinen Prinzen, der von Planet zu Planet reist und viele **interessante** Charaktere trifft. In dem Buch gibt es viele Metaphern und **Symbole**. Eine davon ist der Fuchs, der sagt, **dass** man nur mit **dem** Herz gut sehen kann. **Dieser Satz** bedeutet, **dass** wir die wahren **Werte** der Dinge nur mit **unserem** Herz erkennen können und nicht nur mit **unseren** Augen. Der Autor verwendet **eine** einfache Sprache, aber die Botschaften sind sehr tief. Es gibt auch **schöne Zeichnungen**, die die Geschichte ergänzen. Ich finde es besonders bewegend, wie der kleine Prinz immer nach Freundschaft und Liebe sucht. Allerdings gibt es auch einige Schwierigkeiten beim Lesen des Buches. Die Kapitel sind manchmal sehr **kurz**, was das Lesen schwierig macht, weil man sich nicht lange in die Geschichte vertiefen kann. Auch der Stil von Saint-Exupéry kann für einige Leser etwas zu kindlich **erscheinen**. Trotz **dieser** kleinen Mängel **denke** ich, dass „Der kleine Prinz“ ein **hervorragendes** Buch ist. Es lehrt uns **wichtige** Lektionen über das Leben und unsere Beziehungen zu anderen Menschen. Deshalb ist es immer noch mein Lieblingsbuch.";

        // Regular expression to find text between **
        String regex = "\\*\\*(.*?)\\*\\*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // Replacing text enclosed in **
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, "<span class=\"red\"; style=\"color: red; font-weight: bold\">" + matcher.group(1) + "</span>");
        }
        matcher.appendTail(result);

        // Output the result
        System.out.println(result);
    }
}
