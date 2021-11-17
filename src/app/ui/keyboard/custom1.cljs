(ns app.ui.keyboard.custom1
  (:require
   [steroid.rn.core :as rn]
   [app.font.base :as font]
   [app.text.index :as text]
   [app.components.gesture :as gesture]
   [app.ui.components :as ui]
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.navigation.safe-area :as safe-area]

   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]
   ["react-native-advanced-ripple" :as ripple]))

(def key-style
  {
          :flex-direction "row"
          :flex 1
          :justifyContent "center"
          :alignItems "center"
          :backgroundColor "#FFF"
          :borderRightColor "#e8e8e8"
          :borderRightWidth 1
          :borderBottomColor "#e8e8e8"
          :borderBottomWidth 1})
          ; :height 38})

(def key-con-style
  {:backgroundColor "#FFF"
   :borderRightColor "#e8e8e8"
   :borderRightWidth 1
   :borderBottomColor "#e8e8e8"
   :borderBottomWidth 1
   :flex 1})

(def key-text-style
  {
    :fontWeight "400"
          :fontSize 25,
          :textAlign "center",
          :color "#222222"
          :width 42})

(def key-list [["ᠠ᠊" "ᠡ᠊" "ᠢ᠊" "ᠣ᠊" "ᠤ᠊" "ᠥ᠊" "ᠦ᠊" "ᠨᠠ"]
               ["ᠪᠠ" "ᠫᠠ" "ᠬᠠ" "ᠭᠠ" "ᠮᠠ" "ᠯᠠ" "ᠰᠠ" "ᠱᠠ"]
               ["ᠲᠠ" " ᠳᠠ" "ᠴᠠ" "ᠵᠠ" "ᠶᠠ" "ᠷᠠ"]
               ["ᠸᠠ" "ᠺᠠ" "ᠹᠠ" "ᠽᠠ" "ᠼᠠ" "?"]])

(defn keyboard-view []
  (let [h (reagent/atom nil)]
    (fn []
      [ui/safe-area-consumer
      ; [safe-area/safe-area-view {:style {:flex             1}}
                                       ;:background-color :white}}
       [rn/view {:style {:flex-direction "column" :width "100%" :height "100%"
                         :flex 1}}
        [rn/view {:style {:flex nil
                          :width "100%"
                          :flex-direction "column"
                          :justifyContent "flex-end"}}
         [rn/view {:style {:height 180
                           :backgroundColor "#FFF"
                           :alignItems "center"
                           :justifyContent "center"}}
          (doall (for [k (take 2 key-list)]
                   ^{:key k}
                   [rn/view {:style {:flex 1 :flex-direction "row"
                                     :alignItems "center"
                                     :justifyContent "center"}}
                    (doall (for [kk k]
                             ^{:key kk}
                             [rn/view {:style key-con-style}
                              [:> ripple {:rippleColor "#000" :style key-style}
                               [rn/view {:style {:height "100%" :width "100%"}}
                                [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} kk]]]]))]))
          [rn/view {:style {:flex 2 :flex-direction "row"
                            :alignItems "center"
                            :justifyContent "center"}}
           [rn/view {:style {:flex 6 :flex-direction "column"
                             :alignItems "center"
                             :justifyContent "flex-end"}}
            [rn/view {:style {:flex 1 :flex-direction "row"
                              :alignItems "center"
                              :justifyContent "center"}}
             (doall (for [kk (nth key-list 2)]
                       ^{:key kk}
                       [rn/view {:style key-con-style}
                        [:> ripple {:rippleColor "#000" :style key-style}
                         [rn/view {:style {:height "100%" :width "100%"}}
                          [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} kk]]]]))]
            [rn/view {:style {:flex 1 :flex-direction "row"
                              :alignItems "center"
                              :justifyContent "center"}}
             (doall (for [kk (nth key-list 3)]
                       ^{:key kk}
                       [rn/view {:style key-con-style}
                        [:> ripple {:rippleColor "#000" :style key-style}
                         [rn/view {:style {:height "100%" :width "100%"}}
                          [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} kk]]]]))]]
           [rn/view {:style (merge key-con-style {:flex 1})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/view {:style {:height "100%" :width "100%"}}
              [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} "ᠳᠡᠪᠢᠰᠭᠡᠷ"]]]]
           [rn/view {:style (merge key-con-style {:flex 1})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/view {:style {:height "100%" :width "100%"}}
              [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} "ᠳᠠᠭᠠᠪᠤᠷᠢ"]]]]]
          [rn/view {:style {:flex 1 :flex-direction "row"
                            :alignItems "center"
                            :justifyContent "center"}}
           [rn/view {:style (merge key-con-style {:flex 1})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/text {:style {:height "100%"}} "123"]]]
           [rn/view {:style (merge key-con-style {:flex 1})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/text {:style {:height "100%"}} "abc"]]]
           [rn/view {:style (merge key-con-style {:flex 1.5})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/text {:style {:height "100%"}} "home"]]]
           [rn/view {:style (merge key-con-style {:flex 1.5})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/text {:style {:height "100%"}} "space"]]]
           [rn/view {:style (merge key-con-style {:flex 1.5})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/text {:style {:height "100%"}} "back"]]]
           [rn/view {:style (merge key-con-style {:flex 1.5})}
            [:> ripple {:rippleColor "#000" :style key-style}
             [rn/text {:style {:height "100%"}} "return"]]]]]]]])))
