/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        'staynest-pink': '#FF385C', // Staynest signature coral pink/red
        'staynest-hover': '#FF5A5F', // Hover state coral
        'staynest-light': '#F7F7F7', // Light gray for backgrounds
        'staynest-white': '#FFFFFF', // White for text/icons
      },
    },
  },
  plugins: [],
};