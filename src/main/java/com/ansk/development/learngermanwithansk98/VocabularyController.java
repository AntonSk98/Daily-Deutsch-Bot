package com.ansk.development.learngermanwithansk98;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

@Controller
public class VocabularyController {

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @GetMapping
    public String getVocabulary(Model model) {
        VocabularyCard testCard = getTestCards();
        model.addAttribute("vocabularyCard", testCard);
        return "vocab_template";
    }

    @GetMapping("pdf")
    public ResponseEntity<Void> asPfd() {
        VocabularyCard testCard = getTestCards();
        testToPdf(testCard);
        return ResponseEntity.ok().build();
    }

    private void testToPdf(VocabularyCard card) {
        Context ctx = new Context();
        ctx.setVariable("vocabularyCard", card);
        var html = springTemplateEngine.process("vocab_template", ctx);

        try (OutputStream outputStream = new FileOutputStream("/media/ansk98/D/development/learn-german-with-ansk98/src/main/resources/static/img/test1.pdf")) {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter("/media/ansk98/D/development/learn-german-with-ansk98/src/main/resources/static/img/test1.pdf"));
            HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes()), pdfDoc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private VocabularyCard getTestCards() {
        return new VocabularyCard(
                "TestCard",
                List.of(
                        WordEntry.of(
                                "laufen",
                                "to run",
                                "sich schnell zu Fuß fortbewegen",
                                4,
                                "Er lief so schnell er konnte nach Hause.",
                                List.of(new Variation("er läuft"), new Variation("er lief"), new Variation("er ist gelaufen"))
                        ),
                        WordEntry.of(
                                "das Haus",
                                "house",
                                "Gebäude, in dem Menschen wohnen oder arbeiten",
                                4,
                                "Das Stück wurde monatelang vor ausverkauftem Haus gespielt.",
                                List.of(new Variation("Häuser"))
                        ),
                        WordEntry.of(
                                "schnell",
                                "quick , fast",
                                "mit relativ hoher Geschwindigkeit",
                                4,
                                "Der Zug wurde immer schneller.",
                                "The train got faster and faster.",
                                null
                        ),
                        WordEntry.of(
                                "singen",
                                "to sing",
                                "mit der Stimme eine Melodie erzeugen",
                                3,
                                "Er singt wunderschön / falsch.",
                                "He sings beautifully / out of tune.",
                                List.of(new Variation("er sang"), new Variation("gesungen"))
                        ),
                        WordEntry.of(
                                "der Tisch",
                                "table",
                                "Platte mit vier Beinen, an der man isst, arbeitet usw",
                                4,
                                "den Tisch decken / abräumen",
                                "to set/clear the table",
                                List.of(new Variation("Tische"))
                        ),
                        WordEntry.of(
                                "das Licht",
                                "light",
                                "die Strahlen, die bewirken, dass man sehen kann",
                                4,
                                "im hellen Sonnenlicht",
                                "in bright sunlight",
                                List.of(new Variation("Lichter"))
                        ),
                        WordEntry.of(
                                "schön",
                                "beautiful , lovely , handsome",
                                "gut aussehend",
                                4,
                                "Sie hat so schöne Augen!",
                                "She has such beautiful eyes!",
                                null
                        ),
                        WordEntry.of(
                                "trinken",
                                "to drink",
                                "Flüssigkeit zu sich nehmen",
                                4,
                                "Wollen wir zusammen Kaffee trinken?",
                                null
                        ),
                        WordEntry.of(
                                "das Auto",
                                "car",
                                "von einem Motor angetriebenes Fahrzeug für Personen",
                                4,
                                "mit dem Auto fahren",
                                null
                        ),
                        WordEntry.of(
                                "sich freuen",
                                "to be happy/glad (about) , to be pleased (with)",
                                "Freude empfinden",
                                4,
                                "Ich freue mich sehr über das Geschenk.",
                                "I’m really pleased with the present.",
                                null
                        ),
                        WordEntry.of(
                                "Hoffnung",
                                "hope",
                                "das Hoffen oder was man hofft",
                                5,
                                "Du darfst die Hoffnung nicht aufgeben!",
                                "You mustn’t give up hope.",
                                List.of(new Variation("Hoffnungen"))
                        ),
                        WordEntry.of(
                                "kalt",
                                "cold",
                                "mit niedriger Temperatur",
                                4,
                                "der kälteste Winter seit Jahren",
                                "the coldest winter in years",
                                List.of(new Variation("kälter"), new Variation("kälteste"))
                        ),
                        WordEntry.of(
                                "lernen",
                                "to learn",
                                "sich Wissen, Kenntnisse oder Fähigkeiten aneignen",
                                3,
                                "eine Fremdsprache lernen",
                                null
                        ),
                        WordEntry.of(
                                "der Baum",
                                "tree",
                                "große Pflanze mit einem Stamm und Ästen",
                                4,
                                "auf einen Baum klettern",
                                List.of(new Variation("Bäume"))
                        ),
                        WordEntry.of(
                                "stark",
                                "strong , powerful",
                                "mit viel Kraft",
                                4,
                                "starke Muskeln haben",
                                List.of(new Variation("stärker"), new Variation("stärkste"))
                        ),
                        WordEntry.of(
                                "kochen",
                                "to cook",
                                "warme Speisen zubereiten",
                                3,
                                "das Abendessen kochen",
                                "to cook dinner",
                                null
                        ),
                        WordEntry.of(
                                "das Wasser",
                                "water",
                                "Flüssigkeit, die aus einer Verbindung von Wasserstoff und Sauerstoff besteht",
                                4,
                                "Nach dem Unwetter stand die Wiese unter Wasser.",
                                "The meadow was under water after the storm.",
                                List.of(new Variation("Wässer"))
                        ),
                        WordEntry.of(
                                "die Kunst",
                                "art",
                                "das schöpferische Gestalten mit Worten, Musik, Farben oder Materialien",
                                4,
                                "Kunstgeschichte",
                                List.of(new Variation("Künste"))
                        ),
                        WordEntry.of(
                                "gerade",
                                "straight",
                                "nicht krumm, nicht gebogen",
                                4,
                                "einen Draht gerade biegen",
                                "to straighten a wire",
                                null
                        ),
                        WordEntry.of(
                                "die Reichweite",
                                "range",
                                "Bereich, der erreicht wird",
                                3,
                                "Raketen mit kurzer Reichweite",
                                "short-range missiles",
                                List.of(new Variation("Reichweiten"))
                        )
                ));
    }

    public static class VocabularyCard {
        private String id;
        private String title;
        private List<WordEntry> wordEntries = new ArrayList<>();

        public VocabularyCard() {
        }

        public VocabularyCard(String title, List<WordEntry> wordEntries) {
            this.id = Arrays.stream(UUID.randomUUID().toString().split("-")).reduce((first, second) -> second).orElseThrow();
            this.title = title;
            this.wordEntries = ofNullable(wordEntries).orElse(emptyList());
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public List<WordEntry> getWordEntries() {
            return wordEntries;
        }
    }

    public static class WordEntry {
        private String word;
        private String translation;
        private String meaning;
        private long frequency;
        private String example;
        private String variations;
        private String exampleTranslation;

        public WordEntry() {
        }

        public WordEntry(String word,
                         String translation,
                         String meaning,
                         long frequency,
                         String example,
                         String exampleTranslation,
                         List<Variation> variations) {
            this.word = word;
            this.translation = translation;
            this.meaning = meaning;
            this.frequency = frequency;
            this.example = example;
            this.exampleTranslation = exampleTranslation;
            this.variations = ofNullable(variations).orElse(emptyList()).stream().map(Variation::getVariation).collect(Collectors.joining(" | "));
        }

        public static WordEntry of(String word,
                                   String translation,
                                   String meaning,
                                   long frequency,
                                   String example,
                                   String exampleTranslation,
                                   List<Variation> variations) {
            return new WordEntry(word, translation, meaning, frequency, example, exampleTranslation, variations);
        }

        public static WordEntry of(String word,
                                   String translation,
                                   String meaning,
                                   long frequency,
                                   String example,
                                   List<Variation> variations) {
            return new WordEntry(word, translation, meaning, frequency, example, null, variations);
        }

        public String getWord() {
            return word;
        }

        public String getTranslation() {
            return translation;
        }

        public String getMeaning() {
            return meaning;
        }

        public String getExample() {
            return example;
        }

        public String getVariations() {
            return variations;
        }

        public long getFrequency() {
            return frequency;
        }

        public String getExampleTranslation() {
            return exampleTranslation;
        }
    }

    public static class Variation {
        private String variation;

        public Variation() {
        }

        public Variation(String variation) {
            this.variation = variation;
        }

        public String getVariation() {
            return variation;
        }
    }
}
