(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   [reagent.core :as reagent]
   ["react-native-svg" :as svg]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->clj ->js]]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [app.text.index :as text]))

(defn root-comp []
  [safe-area/safe-area-view
   [rn/view {:style {:flexDirection "column"}}
    [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]]])
    ;; [:> svg/Svg {:width "130" :height "130" :fill "blue" :stroke "red" :color "green" :viewBox "-16 -16 544 544" :style {:transform [{:scaleX -1}]}}
    ; (if (font/get-font :white)
    ;   [rn/view {:style {:margin 10
    ;                     :flex 1
    ;                     :backgroundColor :white}}
    ;
    ;    [:> svg/Svg {:width  "100%"
    ;                 :height "530"
    ;                 :fill   "blue"
    ;                 :color  "green"
    ;                 :top 0
    ;                 :left 0
    ;                 :position  "relative"}
    ;     ; [:> svg/Rect {:x      "0"
    ;     ;               :y      "0"
    ;     ;               :width  "28"
    ;     ;               :height 520
    ;     ;               :fill   "#318FFE"}]
    ;     (map-indexed
    ;      (fn [idx item]
    ;        (map-indexed
    ;         (fn [i run]
    ;           (if-not (empty? (:svg run))
    ;             (let [x (if (= idx 0) "3" (str (* idx 40)))]
    ;               [:> svg/Path {:d        (:svg run)
    ;                             :x        x
    ;                             :y        (str (:y run))
    ;                             :rotation "90"
    ;                             :key      (str idx "-" i)}])))
    ;         item))
    ;      (text/text-component 25.5 520 font/mlongstr))]])
    


(defn init []
  (font/init)

  (if (font/get-font :white) (js/console.log "yes!!!!!") (js/console.log "no !!!!!"))
  (rn/register-comp "cljapp" root-comp))



(comment
  (require '[promesa.core :as p])
  @font/fonts
  (js/console.log "hahah"))
