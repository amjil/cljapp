(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   [reagent.core :as reagent]
   ["react-native-svg" :as svg]
   ["react-native-fs" :as fs]
   ["@pdf-lib/fontkit" :as fontkit]
   ["react-native-reanimated" :default animated :refer [EasingNode useAnimatedStyle useSharedValue withRepeat withTiming]]
   ["react-native-redash/lib/module/v1" :as redash]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->clj ->js]]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [app.text.index :as text]
   [app.components.animated :as animated]))

(def animated-view (reagent/adapt-react-class (.-View animated)))

;; (defn blink-view []
;;   (js/console.log "1111")
;;     (reagent/as-element 
;;   (let [opacity (useSharedValue 0)
;;         _       (js/console.log "2222")
;;         _       (set!
;;                  (.-value opacity)
;;                  (withRepeat
;;                   (withTiming 1 {:duration 1000
;;                                  :easing   (.-ease Easing)})
;;                   (- 1)
;;                   true))
;;         _       (js/console.log "333" (.-value opacity))

;;         _ #(hash-map :opacity (j/get opacity :value))
;;         ;; style   (useAnimatedStyle (fn [] #js {:opacity (.-value opacity)}) #js [] #js [])
;;         style {:opacity (j/get opacity :value)}
;;         _       (js/console.log "444")]
;;     [animated-view {:style {}}
;;      [:> svg/Svg {
;;                   }
;;       [:> svg/Rect {:x      "74"
;;                     :y      "0"
;;                     :fill   "black"
;;                     :width  28
;;                     :height 3}]]])))

(defn blink-view []
  (let [value     (.-Value animated)
             animation (value. 0)
             useCode   (.-useCode animated)
             _         (useCode
                        (fn []
                          (j/call animated :set
                           animation
                           (redash/loop
                            #js
                             {:duration  1000
                              :easing    (.inOut EasingNode (.-ease EasingNode))
                              :boomerang true
                              :autoStart true})))
                        #js [animation])
             opacity   (redash/mix animation 0.1 1)]
         [animated-view {:style opacity}
          [:> svg/Svg {:width  "100%"
                       :height "530"}
           [:> svg/Rect {:x      "74"
                         :y      "0"
                         :fill   "black"
                         :width  28
                         :height 3}]]]))

(defn root-comp []
  [safe-area/safe-area-view
   [rn/view {:style {:flexDirection "column"}}
    [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]
    ;; [:> svg/Svg {:width "130" :height "130" :fill "blue" :stroke "red" :color "green" :viewBox "-16 -16 544 544" :style {:transform [{:scaleX -1}]}}
    (if (font/get-font :white)
      [rn/view {:style {:margin 10
                        :flex 1
                        :backgroundColor :white}}

       [:> svg/Svg {:width  "100%"
                    :height "530"
                    :fill   "blue"
                    :color  "green"
                    :top 0
                    :left 0
                    :position  "relative"}
        [:> svg/Rect {:x      "0"
                      :y      "0"
                      :width  "28"
                      :height 520
                      :fill   "#318FFE"}]
        (map-indexed
         (fn [idx item]
           (map-indexed
            (fn [i run]
              (if-not (empty? (:svg run))
                (let [x (if (= idx 0) "3" (str (* idx 40)))]
                  [:> svg/Path {:d        (:svg run)
                                :x        x
                                :y        (str (:y run))
                                :rotation "90"
                                :key      (str idx "-" i)}])))
            item))
         (text/text-component 25.5 520 font/mlongstr))]
       ]
                      )
                      
                      [:f> blink-view]
                      
                      ]])
        ;;  [:> svg/Path {:d
        ;;                "M7.85 -10.65L7.85 -8.52L5.71 -8.52Q5.68 -8.24 5.64 -7.73Q5.59 -7.23 5.53 -6.69Q5.47 -6.14 5.39 -5.65Q5.31 -5.16 5.23 -4.87L4.82 -4.75Q4.46 -5.62 4.25 -6.59Q4.03 -7.55 3.94 -8.52L2 -8.52Q1.98 -8.24 1.93 -7.73Q1.89 -7.23 1.83 -6.69Q1.77 -6.14 1.69 -5.65Q1.61 -5.16 1.52 -4.87L1.11 -4.75Q0.76 -5.62 0.54 -6.59Q0.33 -7.55 0.23 -8.52L0 -8.52L0 -10.65Z"
        ;;                :x "0" :y "0" ;;:rotation "90"
        ;;                }]



(defn init []
  (font/init)

  (if (font/get-font :white) (js/console.log "yes!!!!!") (js/console.log "no !!!!!"))
  (rn/register-comp "cljapp" root-comp))



(comment
  (require '[promesa.core :as p])
  (require '["react-native-fs" :as fs])
  @font/fonts
  (js/console.log "hahah"))
  
