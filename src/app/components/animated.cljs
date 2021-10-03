(ns app.components.animated
  (:require
   ["react-native-reanimated"
    :default animated
    :refer [Easing useAnimatedStyle useSharedValue withRepeat withTiming]]
   [reagent.core :as reagent]
   [steroid.rn.core :as rn]
   [steroid.rn.components.ui :as ui]
   [applied-science.js-interop :as j]))

(def animated-view (reagent/adapt-react-class (.-View animated)))

(defn animated-box []
  (let [offset         (useSharedValue 0)
        animatedStyles (useAnimatedStyle
                        (fn []
                          {:transform [{:translateX (* (.-value offset) 255)}]}
                          )
                        )]
    [rn/view
     [animated-view {:style animatedStyles}]
     [ui/button {:title "Move"
                 :onPress #(set! (.-value offset) (.random js/Math))}]]))

(comment
  useAnimatedStyle

  animated  (.-View animated)

  (reagent/adapt-react-class (.-View animated))
  (require '[applied-science.js-interop.destructure :as jd])

  (jd/fn [] {:transform [{:translateX (* (.-value offset) 255)}]})

  jd/fn
  jd/let
  j/get

  )


