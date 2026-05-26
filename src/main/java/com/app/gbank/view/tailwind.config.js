const { addDynamicIconSelectors } = require("@iconify/tailwind");
module.exports = {
  darkMode: 'class',
  content: [
    "../../../../../resources/**/**/*.{html,js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        gbank: {
          blue:       "#0066cc",
          teal:       "#00b4d8",
          lightblue:  "#90e0ef",
          dark:       "#03045e",
          soft:       "#caf0f8",
        }
      },
      fontFamily: {
        sans: ["Inter", "ui-sans-serif", "system-ui"]
      }
    }
  },
  plugins: [
    addDynamicIconSelectors(),
  ]
}