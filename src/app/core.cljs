(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [app.font.base :as font]
   [app.ui.views :as views]
   app.events
   app.subs))


(defn init []
  (font/init)

  (rn/register-comp "cljapp" views/root-stack))
