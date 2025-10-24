
# AI Journal Companion (Android · Kotlin · Jetpack Compose)

A lightweight Android app for analysing journal text with an AI backend. Paste or type an entry, send it to the API, and get back the **dominant emotion**, **an emoji**, and short **advice**. The app also visualises your history and lets you search/sort past results for quick insights.

## ✨ Features

- **AI Analysis**: Sends journal text to a FastAPI (or similar) endpoint and receives `{ emotion, emoji, advice }`.
- **Compose UI**: Modern Material 3 interface with Kotlin Coroutines.
- **Charts**: Custom **Pie Chart** visualisation of emotion distribution.
- **History Tools**:
  - Sort using classic algorithms (Bubble / Insertion / Selection).
  - Search via BST / HashMap / Doubly-Linked List strategies.
  - (Drag & Drop) past emotions UI component scaffolded.
- **Help Dialog**: In-app help rendered via a WebView overlay.

## 🗂 Project Structure

```
aijournalcompanion/
├─ api/
│  ├─ ApiModels.kt
│  └─ RestClient.kt
├─ model/
│  └─ EmotionEntry.kt
├─ ui/
│  ├─ chart/
│  │  ├─ DragAndDrop.kt
│  │  └─ PieChart.kt
│  ├─ components/
│  │  ├─ AnalyseBar.kt
│  │  ├─ EmotionLegend.kt
│  │  ├─ HelpDialog.kt
│  │  ├─ SearchDropDown.kt
│  │  └─ SortDropDown.kt
│  ├─ screen/
│  │  └─ MainScreen.kt
│  └─ theme/
│     ├─ Color.kt
│     ├─ Theme.kt
│     └─ Type.kt
├─ utils/
│  ├─ SearchUtils.kt
│  └─ SortUtils.kt
└─ MainActivity.kt
```

## 🔌 Backend API Contract

**Method:** `POST`  
**Body:**
```json
{ "text": "I felt overwhelmed but hopeful after the exam." }
```
**Response:**
```json
{
  "emotion": "HOPEFUL",
  "advice": "Acknowledge the stress and set one small action for tomorrow.",
  "emoji": "🙂"
}
```

## ✅ Requirements

- Android Studio (latest)
- Kotlin + Jetpack Compose
- Internet permission in `AndroidManifest.xml`

## 🚀 Running the App

1. Clone/Import into Android Studio
2. Ensure backend is running (`uvicorn app:app --port 8000`)
3. Set API URL in `RestClient.kt`
4. Run on device/emulator

## 🧩 Notable Components

- AnalyseBar – input + API call handler
- EmotionLegend – chart legend
- HelpDialog – WebView overlay

## 🧪 Extending & Testing

- Add persistence (Room or DataStore)
- Swap `HttpURLConnection` for `OkHttp`
- Add DI (Hilt/Koin)
- Unit test sorting/search utils

## 📄 Licence

MIT
