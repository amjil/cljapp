(ns app.ui.nativebase
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [reagent.core :as reagent]
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.core :as rnn]
   [app.ui.text.index :as text]
   ["react-native-measure-text-chars" :as rntext]
   ["native-base" :refer [NativeBaseProvider
                          Center
                          Container
                          Box
                          Heading
                          Text
                          Button
                          useStyledSystemPropsResolver
                          useThemeProps
                          useToast]]))

(def nativebase-provider (reagent/adapt-react-class NativeBaseProvider))

(def box (reagent/adapt-react-class Box))

(def container (reagent/adapt-react-class Container))

(def heading (reagent/adapt-react-class Heading))

(def text (reagent/adapt-react-class Text))

(def button (reagent/adapt-react-class Button))

(def center (reagent/adapt-react-class Center))

(defn view2 []
  (let [text-props {:fontSize "md" :color :white}
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))]
    [center {:flex 1 :px 3 :safeArea true}
     [box {:bg {:linearGradient {:colors ["lightBlue.300" "violet.800"]
                                 :start [0 0]
                                 :end [1 0]}}
           :p "12"
           :rounded "lg"
           :_text text-props
           :safeArea true}
      [text/text-view (merge {:text
                               "This is a Box with Linear Gradient"
                              :height "auto"}
                             (bean/->clj props))]]]))

(defn toast-render [props text]
  (bean/->js
    {:render
     (fn []
       (reagent/as-element
         [box {:bg "emerald.500" :px "2" :py "1" :rounded "sm" :mb 5}
          [text/text-view (merge {:text
                                  text
                                  :height "auto"}
                                (bean/->clj props))]]))
     :placement :bottom-left}))


(defn toast-view []
  (let [text-props {:fontSize "md" :color :white}
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))
        toast (useToast)]
    (js/console.log "props = " (bean/->js props))
    [center {:flex 1 :px 3 :safeArea true}
     [button
      {:onPress (fn [] (j/call toast
                         :show (toast-render props "Hello! Have a nice day")))}


      "Custom Toast"]]))

(defn container-view []
  (let [text-props (bean/->js (useThemeProps "Heading" #js {:height "80%"}))
        text "A component library for the React Ecosystem"
        [props _] (useStyledSystemPropsResolver (bean/->js text-props))
        data (reagent/atom nil)
        width 200
        height 400
        offset (- (/ height 2) (/ width 2))]

    (js/console.log "props = " (bean/->js props))
    [center {:flex 1 :px 3 :safeArea true}
     [container {:style {:width width
                         :height height}}

      [heading  {:style {:width height :height width
                         :transform [{:rotateY "180deg"}
                                     {:rotate "90deg"}
                                     {:translateX offset}
                                     {:translateY (- (/ offset 2))}]}
                 :selectable true}
       "A component library for the "
       [heading  {:color "emerald.500"} "React Ecosystem"]]]]))


(defn view []
  [center {:flex 1 :px 3 :safeArea true}
   [rn/view {}
    [rn/text "abc"]]])
