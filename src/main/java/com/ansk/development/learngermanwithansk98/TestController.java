package com.ansk.development.learngermanwithansk98;

import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Tmp test controller.
 *
 * @author Anton Skripin
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private final OpenAiGateway aiGateway;

    private static final String TITLE = "Der Einfluss sozialer Medien auf die mentale Gesundheit";

    private static final List<String> QUESTIONS = List.of(
            "Welche positiven Auswirkungen können soziale Medien auf das Gefühl der Verbundenheit haben?",
            "Wie können soziale Medien das Selbstwertgefühl negativ beeinflussen?",
            "Welche Rolle spielt Cybermobbing im Zusammenhang mit sozialen Medien und der mentalen Gesundheit?",
            "Welche Maßnahmen werden im Text vorgeschlagen, um die negativen Effekte sozialer Medien zu reduzieren?",
            "Warum könnte ein digitales Detox hilfreich für die Verbesserung der mentalen Gesundheit sein?"
    );

    private static final Output TEXT_OUTPUT = new Output(
            List.of(
                    "In den letzten Jahren haben soziale Medien einen enormen Einfluss auf unsere Gesellschaft und insbesondere auf die mentale Gesundheit der Menschen. Plattformen wie Facebook, Instagram und Twitter sind aus dem Alltag vieler Menschen nicht mehr wegzudenken. Sie bieten eine Möglichkeit, mit Freunden und Familie in Kontakt zu bleiben, aber sie bringen auch einige Herausforderungen mit sich.",
                    "Einerseits können soziale Medien das Gefühl der Verbundenheit stärken. Viele Nutzer berichten, dass sie durch diese Plattformen neue Freundschaften schließen oder alte Freundschaften pflegen konnten. Diese Art der sozialen Interaktion kann helfen, Einsamkeit zu vermindern und ein Gefühl der Gemeinschaft zu schaffen.",
                    "Andererseits gibt es auch viele negative Aspekte. Die ständige Vergleichbarkeit mit anderen kann zu einem verminderten Selbstwertgefühl führen. Nutzer sehen oft die besten Seiten des Lebens ihrer Freunde oder berühmter Personen und vergleichen dies mit ihrem eigenen Leben. Dies kann Gefühle von Unzulänglichkeit hervorrufen und das Risiko für Depressionen erhöhen.",
                    "Ein weiteres Problem ist die Cybermobbing-Problematik. Besonders Jugendliche sind häufig Opfer von Mobbing im Internet. Die Anonymität der sozialen Medien erleichtert es manchen Nutzern, verletzende Kommentare zu hinterlassen oder andere herabzusetzen. Dies kann schwerwiegende Auswirkungen auf das psychische Wohlbefinden haben und führt nicht selten zu Angstzuständen oder sogar Suizidgedanken.",
                    "Es gibt jedoch Maßnahmen, um diesen negativen Einflüssen entgegenzuwirken. Aufklärung über den verantwortungsvollen Umgang mit sozialen Medien ist notwendig, vor allem in Schulen. Eltern sollten ebenfalls informiert werden, um ihre Kinder angemessen unterstützen zu können. Auch Pausen von sozialen Netzwerken können hilfreich sein; digitales Detox kann dazu beitragen, die eigene mentale Gesundheit zu verbessern.",
                    "Abschließend lässt sich sagen, dass soziale Medien sowohl positive als auch negative Auswirkungen auf die psychische Gesundheit haben können. Es liegt an jedem Einzelnen, bewusst damit umzugehen und Strategien zu entwickeln, um die positiven Aspekte zu nutzen und die negativen Folgen so gut wie möglich zu minimieren."
            )
    );


    private final ReadingExerciseDocumentPipe pipe;

    public TestController(OpenAiGateway aiGateway, ReadingExerciseDocumentPipe pipe) {
        this.aiGateway = aiGateway;
        this.pipe = pipe;
    }


    @GetMapping()
    public ResponseEntity<Void> testFunctionality() throws IOException {


        return ResponseEntity.ok().build();
    }

    public record Output(List<String> text) {
    }
}
