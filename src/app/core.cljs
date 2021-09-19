(ns app.core
  (:require
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.safe-area :as safe-area]
   ["react-native-svg" :as svg]
   ["react-native-fs" :as fs]
   ["@pdf-lib/fontkit" :as fontkit]
   [goog.crypt.base64 :as b64]
   [app.font.base :as font]
   [promesa.core :as p]
   [promesa.async-cljs :refer-macros [async]]
   ))



(defn root-comp []
  [safe-area/safe-area-view
   [rn/view
    [rn/text "Hello CLojure! from CLJS "]
    ;; [:> svg/Svg {:width "130" :height "130" :fill "blue" :stroke "red" :color "green" :viewBox "-16 -16 544 544" :style {:transform [{:scaleX -1}]}}
    [:> svg/Svg {:width "130" :height "130" :fill "blue" :stroke "red" :color "green" :viewBox "16 16 544 544"}
    ;;  [:> svg/Path {:d "M0,0L512,512" :stroke "currentColor" :strokeWidth "32"}]
    ;;  [:> svg/G {:origin "300, 300"}
    ;;   [:> svg/Path {:d "M0,0L512,512" :stroke "currentColor" :strokeWidth "32"}]]
    ;;  [:> svg/G {:origin "310,310"}
     (if (font/get-font :white)
    ;;  [:> svg/G {:x "0" :y "0" :rotation "90"}
     [:> svg/G {:origin "0,0" :rotation "90"}

      [:> svg/Path {:d
                      ;; "M-574 909L-574 727L-382 727Q-379 652 -368.5 574Q-358 496 -345 417L-309 407Q-289 450 -275 502Q-261 554 -251 604Q-241 654 -236.5 695.5Q-232 737 -232 759L-232 909Q-232 949 -222.5 973.5Q-213 998 -190 998Q-174 998 -161.5 995.5Q-149 993 -137.5 988Q-126 983 -114 976Q-102 969 -87 959Q-62 942 -48.5 929.5Q-35 917 -28 917Q-17 927 -8.5 939.5Q0 952 0 961Q-11 972 -25 989Q-39 1006 -53 1022Q-120 1099 -167 1133Q-214 1167 -250 1167Q-295 1167 -320 1122.5Q-345 1078 -345 1006L-345 909Z"
                      ;; "M33.63 53.26L33.63 42.6L22.38 42.6Q22.21 38.2 21.59 33.63Q20.98 29.06 20.21 24.43L18.11 23.85Q16.93 26.37 16.11 29.41Q15.29 32.46 14.71 35.39Q14.12 38.32 13.86 40.75Q13.59 43.18 13.59 44.47L13.59 53.26Q13.59 55.61 13.04 57.04Q12.48 58.48 11.13 58.48Q10.2 58.48 9.46 58.33Q8.73 58.18 8.06 57.89Q7.38 57.6 6.68 57.19Q5.98 56.78 5.1 56.19Q3.63 55.2 2.84 54.46Q2.05 53.73 1.64 53.73Q1 54.32 0.5 55.05Q0 55.78 0 56.31Q0.64 56.95 1.46 57.95Q2.29 58.95 3.11 59.88Q7.03 64.39 9.79 66.39Q12.54 68.38 14.65 68.38Q17.29 68.38 18.75 65.77Q20.21 63.16 20.21 58.95L20.21 53.26Z"
                      ;; "M-33.63 53.26L-33.63 42.6L-22.38 42.6Q-22.21 38.2 -21.59 33.63Q-20.98 29.06 -20.21 24.43L-18.11 23.85Q-16.93 26.37 -16.11 29.41Q-15.29 32.46 -14.71 35.39Q-14.12 38.32 -13.86 40.75Q-13.59 43.18 -13.59 44.47L-13.59 53.26Q-13.59 55.61 -13.04 57.04Q-12.48 58.48 -11.13 58.48Q-10.2 58.48 -9.46 58.33Q-8.73 58.18 -8.06 57.89Q-7.38 57.6 -6.68 57.19Q-5.98 56.78 -5.1 56.19Q-3.63 55.2 -2.84 54.46Q-2.05 53.73 -1.64 53.73Q-1 54.32 -0.5 55.05Q0 55.78 0 56.31Q-0.64 56.95 -1.46 57.95Q-2.29 58.95 -3.11 59.88Q-7.03 64.39 -9.79 66.39Q-12.54 68.38 -14.65 68.38Q-17.29 68.38 -18.75 65.77Q-20.21 63.16 -20.21 58.95L-20.21 53.26Z"
                    (font/msvg)
                    :stroke "currentColor" :strokeWidth "2"}]])]]]
    )

(defn init []
;;   (async
;;    (->
;;     (.then (.readFileAssets fs "monbaiti.ttf" "base64")
;;            (fn [res] (swap! font/fonts assoc :white (fontkit/create (b64/decodeStringToUint8Array res)))))
;;     js/Promise.resolve
;;     p/await)
;; )

  ;; (if (font/units-per-em :white)
  ;;   (js/console.log (font/units-per-em :whtie))
  ;;   (.then (.readFileAssets fs "monbaiti.ttf" "base64") (fn [res] (swap! font/fonts assoc :white (fontkit/create (b64/decodeStringToUint8Array res))))))
  (font/init)
  ;; (p/alet [result (p/await (.readFileAssets fs "monbaiti.ttf" "base64"))]
  ;;         (swap! font/fonts assoc :white (b64/decodeStringToUint8Array result)))

  (if (font/get-font :white) (js/console.log "yes!!!!!") (js/console.log "no !!!!!"))
  (rn/register-comp "cljapp" root-comp))



(comment 
(require '[promesa.core :as p])
(require '["react-native-fs" :as fs]) 
@font/fonts  
(js/console.log "hahah")  
(p/alet [result (p/await (.readFileAssets fs "monbaiti.ttf" "base64"))]
(js/console.log (b64/decodeStringToUint8Array result))        )
(p/alet [result
(->  
(p/promise (.readFileAssets fs "monbaiti.ttf" "base64")) 
(p/then (fn [res] (swap! font/fonts assoc :cc (fontkit/create (b64/decodeStringToUint8Array res)))))
;; (p/await)

)]
(js/console.log "hahhaha" + result)       )
(async   
(->  
(p/promise (.readFileAssets fs "monbaiti.ttf" "base64")) 
(p/then (fn [res] (swap! font/fonts assoc :bb (fontkit/create (b64/decodeStringToUint8Array res)))))
p/await
)
 )
)
