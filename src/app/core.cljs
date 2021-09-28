(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   ["react-native-svg" :as svg]
   ["react-native-fs" :as fs]
   ["@pdf-lib/fontkit" :as fontkit]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [app.text.index :as text]
   [promesa.core :as p]
   [promesa.async-cljs :refer-macros [async]]))


(defn root-comp []
  [safe-area/safe-area-view
   [rn/view {:style {:flexDirection "column"}}
    [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]
    ;; [:> svg/Svg {:width "130" :height "130" :fill "blue" :stroke "red" :color "green" :viewBox "-16 -16 544 544" :style {:transform [{:scaleX -1}]}}
    (if (font/get-font :white)
      [rn/view {:style {:margin          10
                        ;; :backgroundColor "yellow"
                        }}
       [:> svg/Svg {:width  "130"
                    :height "530"
                    :fill   "blue"
                    :color  "green"}
        (map-indexed (fn [idx item]
                       (for [run item]
                         (if-not (empty? (:svg run))
                           (let [x (if (= idx 0) "3" (str (* idx 40)))]
                             [:> svg/Path {:d        (:svg run)
                                           :x        x 
                                           :y        (str (:y run))
                                           :rotation "90"
                                           :key      (str idx (:y run))}])
                           )))
                     (text/text-component 25.5 520 font/mlongstr))
        ;;  [:> svg/Path {:d
        ;;                "M7.85 -10.65L7.85 -8.52L5.71 -8.52Q5.68 -8.24 5.64 -7.73Q5.59 -7.23 5.53 -6.69Q5.47 -6.14 5.39 -5.65Q5.31 -5.16 5.23 -4.87L4.82 -4.75Q4.46 -5.62 4.25 -6.59Q4.03 -7.55 3.94 -8.52L2 -8.52Q1.98 -8.24 1.93 -7.73Q1.89 -7.23 1.83 -6.69Q1.77 -6.14 1.69 -5.65Q1.61 -5.16 1.52 -4.87L1.11 -4.75Q0.76 -5.62 0.54 -6.59Q0.33 -7.55 0.23 -8.52L0 -8.52L0 -10.65Z"
        ;;                :x "0" :y "0" ;;:rotation "90"
        ;;                }]
        ]])]])


(defn init []
  (font/init)

  (if (font/get-font :white) (js/console.log "yes!!!!!") (js/console.log "no !!!!!"))
  (rn/register-comp "cljapp" root-comp))



(comment
  (require '[promesa.core :as p])
  (require '["react-native-fs" :as fs])
  @font/fonts
  (js/console.log "hahah")
  (p/alet [result (p/await (.readFileAssets fs "monbaiti.ttf" "base64"))]
          (js/console.log (b64/decodeStringToUint8Array result)))
  (p/alet [result
           (->
            (p/promise (.readFileAssets fs "monbaiti.ttf" "base64"))
            (p/then (fn [res] (swap! font/fonts assoc :cc (fontkit/create (b64/decodeStringToUint8Array res))))))]
;; (p/await)


          (js/console.log "hahhaha" + result))
  (async
   (->
    (p/promise (.readFileAssets fs "monbaiti.ttf" "base64"))
    (p/then (fn [res] (swap! font/fonts assoc :bb (fontkit/create (b64/decodeStringToUint8Array res)))))
    p/await)))
