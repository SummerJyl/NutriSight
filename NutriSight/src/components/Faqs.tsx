// src/pages/Faqs.tsx
import { useState } from 'react';

const faqs = [
  {
    question: 'What is the Bio Health Data Explorer?',
    answer: 'A tool to search and explore nutritional data from the USDA food database.',
  },
  {
    question: 'Where does the nutritional data come from?',
    answer:
      'The data is sourced directly from the USDA FoodData Central API, which is highly reliable.',
  },
  {
    question: 'How do I search for foods?',
    answer: 'Use the search bar on the home page. You can filter results by nutrient categories.',
  },
  {
    question: 'What nutrients can I explore?',
    answer: 'Protein, Fat, Carbohydrates, Vitamins, Minerals, and more.',
  },
  {
    question: 'Can I save my favorite foods?',
    answer: 'Currently, favorites are not supported, but this feature is planned.',
  },
  {
    question: 'Is my data private?',
    answer: 'Yes, we do not store any personal data from your searches.',
  },
  {
    question: 'How accurate is the data?',
    answer: 'Data is maintained by the USDA and is updated regularly for accuracy.',
  },
  {
    question: 'Who can I contact for support?',
    answer: 'Please email support@biohealthexplorer.com.',
  },
];

const Faqs = () => {
  const [openIndex, setOpenIndex] = useState<number | null>(null);

  const toggle = (index: number) => {
    setOpenIndex(openIndex === index ? null : index);
  };

  return (
    <main style={{ maxWidth: 720, margin: '2rem auto', padding: '0 1rem' }}>
      <h1>Frequently Asked Questions</h1>
      <div>
        {faqs.map((faq, i) => (
          <div
            key={i}
            style={{
              marginBottom: '1rem',
              borderRadius: 8,
              border: '1px solid #ccc',
              overflow: 'hidden',
              boxShadow: openIndex === i ? '0 0 8px #319795aa' : undefined,
            }}
          >
            <button
              onClick={() => toggle(i)}
              aria-expanded={openIndex === i}
              style={{
                width: '100%',
                textAlign: 'left',
                background: '#2c7a7b',
                color: 'white',
                border: 'none',
                padding: '1rem',
                fontSize: '1.1rem',
                cursor: 'pointer',
              }}
            >
              {faq.question}
            </button>
            {openIndex === i && (
              <div
                style={{
                  padding: '1rem',
                  backgroundColor: '#f7fafc',
                  color: '#333',
                  lineHeight: 1.5,
                }}
              >
                {faq.answer}
              </div>
            )}
          </div>
        ))}
      </div>
    </main>
  );
};

export default Faqs;
