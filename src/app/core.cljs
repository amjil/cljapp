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
   [promesa.async-cljs :refer-macros [async]]))


(defn root-comp []
  [safe-area/safe-area-view
   [rn/view
    [rn/text "Hello CLojure! from CLJS "]
    ;; [:> svg/Svg {:width "130" :height "130" :fill "blue" :stroke "red" :color "green" :viewBox "-16 -16 544 544" :style {:transform [{:scaleX -1}]}}
    (if (font/get-font :white)
      [:> svg/Svg {:width "130" :height "130" :fill "blue" :color "green" :viewBox "16 16 544 544"}
       [:> svg/Path {:d
                     (font/msvg)
                     :origin "10,50" :rotation "90"}]])]])



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
