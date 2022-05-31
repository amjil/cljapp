(ns app.ui.basic.theme
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]

   ["@react-navigation/native" :refer [DefaultTheme DarkTheme]]
   ["react-native" :refer [Appearance]]))


; addChangeListener getColorScheme
(def theme (reagent/atom DefaultTheme))

(defn get-system-theme []
  (j/call Appearance :getColorScheme))

(defn update-theme [m]
  (let [them (condp m
               "dark" DarkTheme
               "light" DefaultTheme
               "system" (condp (get-system-theme)
                          "dark" DarkTheme
                          DefaultTheme)
               DefaultTheme)]
    (reset! theme them)))
