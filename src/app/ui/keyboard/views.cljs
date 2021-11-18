(ns app.ui.keyboard.views
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [steroid.rn.core :as rn]
   [app.font.base :as font]
   [app.text.index :as text]
   [app.components.gesture :as gesture]
   [app.ui.components :as ui]
   [app.ui.keyboard.candidates :as candidates]
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.navigation.safe-area :as safe-area]
   [app.handler.keyboard.events :as events]

   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]
   ["react-native-advanced-ripple" :as ripple]))

(defn text-editor [height text]
  (let [[line-width text-svgs] 
        (text/text-component {:width 0 :fill "black" :color "black" :height height :font :white :font-size 20} 
                             text)
        runs (last (last text-svgs))
        x (reagent/atom nil)
        y (reagent/atom nil)
        blink-cursor? (reagent/atom true)
        touch-state (reagent/atom 0)
        line-width (/ line-width 2)]
    (reset! y (:y runs))
    (reset! x (+ line-width (* 32 (dec (count text-svgs)))))
    (fn []
      [rn/view {}
       [gesture/tap-gesture-handler
        {:onHandlerStateChange #(if (gesture/tap-state-end (j/get % :nativeEvent))
                                  (let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                     ; (js/console.log "x = " ex " y = " ey)
                                    (reset! blink-cursor? true)
                                    (reset! x (+ line-width ex))
                                    (reset! y ey)))}
        [gesture/pan-gesture-handler {:onGestureEvent #(let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                                        ; (js/console.log "x = " ex " y = " ey)
                                                         (reset! blink-cursor? true)
                                                         (reset! x (+ line-width ex))
                                                         (reset! y ey))}
         [rn/view {:style {:height "100%" :width "100%"}}
          (when @blink-cursor?
            [:> blinkview {"useNativeDriver" false}
             [rn/view {:style {:position :absolute :top (or @y 0) :left (or @x 0)}}
              [:> svg/Svg {:width 32 :height 2}
               [:> svg/Rect {:x "0" :y "0" :width 32 :height 2 :fill "blue"}]]]])
          [rn-list/flat-list
           {:key-fn    (fn [item index] (str "editor-" index))
            :data      text-svgs
           ; :render-fn text-list-item
            :render-fn (partial text/flat-list-item {:width 32 :fill "gray" :color "black" :height height :font :white :font-size 24})
            
            :horizontal true
            :initialNumToRender 20
           ; :onScroll #(do
           ;              (js/console.log "on scroll >>>>"))
            :on-touch-start #(reset! blink-cursor? false)
            :on-touch-move #(do
                              (reset! blink-cursor? false)
                              (js/console.log "on touch move"))
           ; :onResponderMove #(js/console.log "on responder move >>>")
           ; :onMomentumScrollBegin #(do
           ;                           (js/console.log "onmomentum scroll start >>>"))
            :onMomentumScrollEnd #(do
                                    (swap! blink-cursor? not)
                                    (js/console.log "onmomentum scroll end >>>"))
            :onScrollBeginDrag #(do (reset! blink-cursor? false)
                                    (js/console.log "on scroll begin >>>"))
            :onScrollEndDrag #(do
                                (js/console.log "on scroll end >>>"))}]]]]
       [candidates/views]])))

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

(def key-list [[{:label "ᠣ" :code "q"} {:label "ᠸ᠊" :code "w"} {:label "ᠡ" :code "e"} 
                {:label "ᠷ᠊" :code "r"} {:label "ᠲ᠊" :code "t"} {:label "ᠶ᠊" :code "y"} 
                {:label "ᠦ᠊" :code "u"} {:label "ᠢ" :code "i"} {:label "ᠥ" :code "o"} {:label "ᠫ᠊" :code "p"}]
               [{:label "ᠠ" :code "a"} {:label "ᠰ᠊" :code "s"} {:label "ᠳ" :code "d"} {:label "ᠹ᠊" :code "f"} 
                {:label "ᠭ᠊" :code "g"} {:label "ᠬ᠊" :code "h"} {:label "ᠵ᠊" :code "j"} 
                {:label "ᠺ᠊" :code "k"} {:label "ᠯ᠊" :code "l"} {:label " ᠩ" :code "ng"}]
               [{:label "ᠽ᠊" :code "z"} {:label "ᠱ᠊" :code "x"} {:label "ᠴ᠊" :code "c"}
                {:label "ᠤ᠊" :code "v"} {:label "ᠪ᠊" :code "b"}
                {:label "ᠨ᠊" :code "n"} {:label "ᠮ᠊" :code "m"}]])

(defn keyboard-view []
  (let [h (reagent/atom nil)]
    (fn []
      [ui/safe-area-consumer
       [rn/view {:style {:flex-direction "column" :width "100%" :height "100%"
                         :flex 1}}
        ;; editor
        [rn/view {:style {:width "100%"
                          :flex 2}
                  :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                                (reset! h height))}
         (when @h
           [text-editor @h font/mlongstr])]
        ;; keyboard
        [rn/view {:style {:flex nil
                          :width "100%"
                          :flex-direction "column"
                          :justifyContent "flex-end"
                          :height 180
                          :borderTopWidth 1
                          :borderTopColor "#e8e8e8"}}
         ; [rn/view {:style {:height 180
         ;                   :backgroundColor "#FFF"
         ;                   :alignItems "center"
         ;                   :justifyContent "center"}}
          (doall (for [k (take 2 key-list)]
                   ^{:key k}
                   [rn/view {:style {:flex 1 :flex-direction "row"
                                     :alignItems "center"
                                     :justifyContent "center"}}
                    (doall (for [kk k]
                             ^{:key kk}
                             [rn/view {:style key-con-style}
                              [:> ripple {:rippleColor "#000" :style key-style
                                          :on-press #(dispatch [:candidates-index-concat (:code kk)])}
                               [rn/view {:style {:height "100%" :width "100%"
                                                 :alignItems "center"
                                                 :justifyContent "center"}}
                                [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} (:label kk)]]]]))]))
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [ui/ion-icons {:name "ios-arrow-up-circle-outline" :color "gray" :size 30}]]]]
          (doall (for [kk (nth key-list 2)]
                   ^{:key kk}
                   [rn/view {:style key-con-style}
                    [:> ripple {:rippleColor "#000" :style key-style
                                :on-press #(dispatch [:candidates-index-concat (:code kk)])}
                     [rn/view {:style {:height "100%" :width "100%"
                                       :alignItems "center"
                                       :justifyContent "center"}} 
                      [text/text-inline {:width 38 :fill "black" :font :white :font-size 18} (:label kk)]]]]))
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style
                       :on-press #(dispatch [:keyboard-delete])}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [ui/ion-icons {:name "backspace" :color "gray" :size 30}]]]]]
         [rn/view {:style {:flex 1 :flex-direction "row"
                           :alignItems "center"
                           :justifyContent "center"}}
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [rn/text {} "123"]]]]
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [ui/ion-icons {:name "globe" :color "gray" :size 30}]]]]
          [rn/view {:style (merge key-con-style {:flex 1})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [rn/text {} ""]]]]
          [rn/view {:style (merge key-con-style {:flex 3.5})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [ui/ion-icons {:name "ios-scan" :color "gray" :size 30}]]]]
          [rn/view {:style (merge key-con-style {:flex 1})}
           [:> ripple {:rippleColor "#000" :style key-style}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [rn/text {} ""]]]]
          [rn/view {:style (merge key-con-style {:flex 1.5})}
           [:> ripple {:rippleColor "#000" :style key-style
                       :on-press #(dispatch [:candidates-query 2])}
            [rn/view {:style {:height "100%" :alignItems "center" :justifyContent "center"}}
             [ui/ion-icons {:name "ios-return-down-back-sharp" :color "gray" :size 30}]]]]]]]])))
