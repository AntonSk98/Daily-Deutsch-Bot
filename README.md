
# 🇩🇪 Daily Deutsch Bot
> ***Your friendly Telegram companion for boosting German skills with vocab cards, reading, writing, and listening exercises!***

🎓 [**Join the Chat & Start Learning**](https://t.me/daily_deutsch_group)

---

## 📚 Table of Contents
1. [🧠 Word Cards](#1--word-cards)
2. [📖 Reading Exercise](#2--reading-exercise)
3. [✍️ Writing Exercise](#3--writing-exercise)
4. [🛠 Writing Correction](#4--writing-correction)
5. [🎧 Listening Exercise](#5--listening-exercise)
6. [💡 Dynamic Info](#6--dynamic-information)
7. [🧾 API Summary](#7--api-summary)
8. [💻 For Developers](#8--for-developers)  
   8.1. [🚀 Starting the Bot](#81--starting-the-bot)  
   8.2. [📦 Deploying the Bot](#82--deploying-the-bot)

---

## 1. 🧠 Word Cards
Kickstart your learning with custom **vocab cards** in two styles:

- 🤖 **AI-Assist Mode**  
  Use `/ai_new_word` — just send a German word and let the bot do the rest.

- 🗃 **View Drafts**  
  List your unpublished cards with `/cached_words`.

- 📤 **Publish to PDF**  
  Use `/publish_word_card` to bundle your saved cards into a beautifully formatted PDF — sent right to your Telegram group or chat.

---

## 2. 📖 Reading Exercise
Build your comprehension skills with:

- ⚙️ **Auto-Generated Exercises**  
  `/generate_reading_exercise` — choose your CEFR level and topic. You'll get a unique reading text, questions, and answers — all in one PDF!

- ✍️ **Custom Input Mode**  
  `/create_reading_exercise` — provide your own text, and the bot creates the questions + answer key.

---

## 3. ✍️ Writing Exercise
Sharpen your writing with tailored tasks:

- 📌 **Generate a Task + Sample**  
  Use `/writing_with_example` with your CEFR level and topic to get a prompt *and* a model response.

---

## 4. 🛠 Writing Correction
Get feedback on your written German:

- 📝 **Submit for Correction**  
  Use `/correct_text` — highlight changes with `++added++` or `--removed--`.

- 📬 **Receive a Marked-Up PDF**  
  You'll get a correction breakdown, either as a document or inline message.

> 🤖 *Automated correction is coming soon! Stay tuned.*

---

## 5. 🎧 Listening Exercise
Train your ear with real-world German:

- 🎙 **Create an Exercise from Audio**  
  `/listening_exercise` — upload an audio clip, and the bot provides a transcript, questions, and answers.

- 🛠 **Edit the Transcript**  
  Need to fix typos? Use `/listening_edit_transcription` to revise the text.

---

## 6. 💡 Dynamic Information
Got a German grammar or vocab question?

- 💬 **Ask Anything!**  
  Use `/dynamic_info` — the bot gives instant, context-rich answers with examples.

**Example:**
<pre>
🔍 🧐 Warum brauchen Deutschlernende manchmal ein Navi, um das Verb zu finden?
3️⃣... 2️⃣... 1️⃣...

✍️ Das Verb im Deutschen kann an verschiedenen Stellen im Satz stehen und sich je nach Zeitform oder Satztyp verändern.
Diese Flexibilität macht es für Lernende oft schwierig, das richtige Verb in der passenden Form zu erkennen und korrekt zu verwenden.
Zudem gibt es viele unregelmäßige Verben und verschiedene Konjugationsmuster, die nicht immer intuitiv sind.
Wenn Lernende also versuchen, einen Satz zu bilden oder ihn zu analysieren, können sie sich manchmal wie in einer Stadt ohne klare
Straßenführung fühlen – daher das Bild des Navigationssystems (Navi).
</pre>

---

## 7. 🧾 API Summary

| 💬 Command                        | 🔍 Description                                                  |
|----------------------------------|------------------------------------------------------------------|
| `/new_word`                      | Add a new vocab card manually                                   |
| `/ai_new_word`                   | Add a vocab card using AI assistance                            |
| `/cached_words`                  | View all unpublished word cards                                 |
| `/publish_word_card`             | Export vocab cards as a formatted PDF                           |
| `/generate_reading_exercise`     | Auto-generate a reading text with questions                     |
| `/create_reading_exercise`       | Create a reading quiz from your own text                        |
| `/writing_with_example`          | Get a writing prompt + sample response                          |
| `/correct_text`                  | Submit a written text for manual correction                     |
| `/listening_exercise`            | Create listening questions from uploaded audio                  |
| `/listening_edit_transcription`  | Edit generated transcript                                       |
| `/dynamic_info`                  | Ask anything about German grammar or vocab                      |

---

## 8. 💻 For Developers

### 8.1 🚀 Starting the Bot

You'll need:

- 🔐 **Telegram Bot Token**  
  [Create one here](https://core.telegram.org/bots/tutorial), then set:
  ```bash
  export BOT_TOKEN="<YOUR TELEGRAM BOT TOKEN>"
  ```

- 🧠 **OpenAI API Key**  
  [Get your key](https://platform.openai.com/signup), then set:
  ```bash
  export AI_TOKEN="<YOUR OPENAI API KEY>"
  ```

- 💬 **Target Group or Chat ID**  
  Used for publishing exercises:
  ```bash
  export GROUP_ID="<YOUR GROUP|CHAT ID>"
  ```

- 🖋 **PDF Fonts Path**  
  By default, fonts used for PDF generation are located in the [resource folder](src/main/resources/static/fonts)

  However, you can **override this path** by setting the `RESOURCE_FOLDER` environment variable to point to your custom fonts directory.  
  ```bash
    export RESOURCE_FOLDER="/path/to/fonts"
  ```

- 👤 **Restrict Access**  
  Limit bot usage to your chat ID with the bot:
  ```bash
  export BOT_VERIFIED_USER="<YOUR TELEGRAM USER ID>"
  ```

- 🔐 **REST Auth (Advanced)**  
  For internal REST endpoints, you must provide the following environment variables. These will be used for **Basic Authentication** to secure access:
```bash
  export REST_INTERNAL_SECURITY_USERNAME="<USER>"
  export REST_INTERNAL_SECURITY_PASSWORD="<PASS>"
  ```

---

### 8.2 📦 Deploying the Bot

Use Docker to spin things up:

```bash
docker run -d --network host --restart unless-stopped --name "${CONTAINER_NAME}" \
  -e AI_TOKEN="${AI_TOKEN}" \
  -e GROUP_ID="${GROUP_ID}" \
  -e REST_INTERNAL_SECURITY_USERNAME="${REST_INTERNAL_SECURITY_USERNAME}" \
  -e REST_INTERNAL_SECURITY_PASSWORD="${REST_INTERNAL_SECURITY_PASSWORD}" \
  -e BOT_VERIFIED_USER="${BOT_VERIFIED_USER}" \
  -e BOT_TOKEN="${BOT_TOKEN}" \
  -e RESOURCE_FOLDER="${RESOURCE_FOLDER}" \
  "${CONTAINER_NAME}:latest"
```

[Dockefile](Dockerfile)

🔁 **CI/CD Ready**  
Easily set up your own CI/CD pipeline using the included GitHub Actions workflow.  
The workflow configuration is located [here](.github/workflows/release_pipeline.yml)

---

## 💌 Questions or Feedback?

Feel free to reach out:  
📧 **anton.tech98@gmail.com**  
Or join the conversation on Telegram:  
👉 [t.me/daily_deutsch_group](https://t.me/daily_deutsch_group)

---

**Happy Learning — Viel Erfolg beim Deutschlernen! 🇩🇪🎯**