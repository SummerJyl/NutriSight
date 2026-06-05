# 🧬 NutriSight — Bio Health Data Explorer

> **[🚀 Live Demo](https://summerjyl.github.io/NutriSight/)** | Full-stack personal health platform for tracking nutrition, glucose, and macronutrient goals

A production-grade biohealth application built with React + TypeScript, Spring Boot (Java), and an ML insights layer. Inspired by clinical use cases in MedTech and digital health — designed with healthcare-grade security, performance, and data integrity in mind.

-----

## ✨ Features

### 📊 Interactive Health Dashboard

- Personalized user profiles with custom health goal tracking
- Real-time nutritional data from the **USDA FoodData Central API**
- Advanced search and filtering by nutrient types (protein, vitamins, minerals)

### 📈 Data Visualizations

- **GlucoseInsights** — glucose trend tracking and analysis
- **MacroPieChart** — macro breakdown (protein, fat, carbohydrates)
- **NutrientChart** — detailed nutrient comparison across foods
- Custom D3.js tooltips and responsive chart components

### 🤖 ML Insights Layer (`nutrisight-ml`)

- Machine learning module for nutritional pattern analysis
- Health recommendations based on user goals and tracked data

### 🏗️ Engineering Quality

- Modular React components built with TypeScript
- Optimized hooks and state management
- Unit and integration testing with Jest and React Testing Library
- ESLint + Prettier configured for code quality
- Fully responsive design with Tailwind CSS

-----

## 🧱 Tech Stack

|Layer         |Technology                                    |
|--------------|----------------------------------------------|
|**Frontend**  |React 18, TypeScript, Tailwind CSS, D3.js     |
|**Backend**   |Spring Boot, Java, RESTful APIs               |
|**Data Layer**|USDA FoodData Central API, OpenAPI Integration|
|**ML Layer**  |Python, nutrisight-ml module                  |
|**Dev Tools** |Vite, VS Code, Git/GitHub                     |
|**Deployment**|GitHub Pages                                  |

-----

## 🏛️ App Architecture

```
NutriSight/
├── src/
│   └── components/
│       ├── ChartCard.tsx          # Reusable chart container
│       ├── CustomTooltip.tsx      # D3.js custom tooltips
│       ├── FoodCard.tsx           # Food item display
│       ├── FoodDetailsModal.tsx   # Detailed nutrient modal
│       ├── GlucoseInsights.tsx    # Glucose tracking & trends
│       ├── MacroPieChart.tsx      # Macro breakdown chart
│       ├── NutrientChart.tsx      # Nutrient comparison chart
│       └── Faqs.tsx               # User onboarding/help
├── backend/                       # Spring Boot Java API
├── nutrisight-ml/                 # ML insights module
└── public/
```

-----

## 🚀 Getting Started

### Prerequisites

- Node.js 18+
- Java 17+ (for backend)
- Python 3.10+ (for ML module)

### Frontend

```bash
git clone https://github.com/SummerJyl/NutriSight.git
cd NutriSight
npm install
npm run dev
```

### Backend

```bash
cd backend
./gradlew bootRun
```

### ML Module

```bash
cd nutrisight-ml
pip install -r requirements.txt
python main.py
```

-----

## 🔑 Environment Variables

Create a `.env` file in the root directory:

```env
VITE_USDA_API_KEY=your_usda_api_key_here
VITE_API_BASE_URL=http://localhost:8080
```

Get your free USDA FoodData Central API key at [fdc.nal.usda.gov](https://fdc.nal.usda.gov/api-guide.html).

-----

## 🧪 Testing

```bash
# Run unit and integration tests
npm test

# Run with coverage
npm run test:coverage
```

-----

## 🛡️ Design Principles

This project was built with clinical-grade standards in mind:

- **Security** — Input validation, secure API key handling, CORS configuration
- **Data Integrity** — All nutritional data sourced directly from the official USDA database
- **Performance** — Optimized React rendering, lazy loading, and efficient state management
- **Scalability** — Modular architecture designed for extension into full EHR/FHIR integration

-----

## 🗺️ Roadmap

- [ ] FHIR-compliant nutrient export endpoint
- [ ] OAuth2 authentication (Google, Apple Health)
- [ ] PostgreSQL persistence layer for user history
- [ ] Mobile-responsive PWA
- [ ] Wearable device integration (glucose monitors, fitness trackers)

-----

## 👩‍💻 Author

**Jylian Summers** — Senior Backend Engineer  
[LinkedIn](https://linkedin.com/in/jyliansummers) | [Portfolio](https://summerjyl.github.io/NutriSight/)

-----

## 📄 License

MIT License — see [LICENSE](./LICENSE) for details.

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn package manager

### Installation

```bash
# Clone the repository
git clone https://github.com/SummerJyl/Bio-Health-Data-Explorer.git

# Navigate to the project directory
cd Bio-Health-Data-Explorer

# Install dependencies
npm install

# Start development server
npm run dev


