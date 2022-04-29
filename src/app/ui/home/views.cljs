(ns app.ui.home.views
  (:require
   [reagent.core :as reagent]
   [app.ui.components :as ui]
   [app.ui.text :as text]
   [app.ui.nativebase :as nbase]
   [cljs-bean.core :as bean]
   [applied-science.js-interop :as j]
   [steroid.rn.core :as rn]
   [steroid.rn.components.list :as rnlist]
   [steroid.rn.components.touchable :as touchable]

   [app.handler.animatable :as animatable]))


; (def SendBox (.-default (js/require "../src/js/sendbox.js")))
; (def Refresh (.-default (js/require "../src/js/refresh.js")))





(defn home []
  (let [scale-value (reagent/atom 1)]
    (fn []
      [ui/safe-area-consumer
       [nbase/box {:flex 1
                   :mt 20}
        [nbase/center
         ; [animatable/text {:animation "slideInDown" :iterationCount 5 :direction "alternate"}
         ;  "Up and down you go"]
         [animatable/text {:animation "pulse" :easing "ease-out" :iterationCount "infinite" :style {:textAlign "center"}} "❤️"]
         [nbase/hstack {:my 2
                        :justifyContent "space-between"}
          [rn/touchable-highlight {:on-press #(reset! scale-value (+ @scale-value 2))}
           [text/measured-text {} "button  +"]]
          [rn/touchable-highlight {:on-press #(reset! scale-value (- @scale-value 2))}
           [text/measured-text {} "button  1"]]
          [animatable/view {:transition "scale" :style {:transform [{:scale @scale-value}]
                                                        :margin-left 60}}
           [text/measured-text {} "text to scale"]]]]]])))
    ; [text/measured-text "Up and down you go"]]])
   ; [:> SendBox]])
   ; [Refresh]])
