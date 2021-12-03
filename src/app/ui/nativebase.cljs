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
                          Pressable
                          Spacer
                          HStack
                          VStack
                          ZStack
                          Flex
                          useStyledSystemPropsResolver
                          useThemeProps
                          useToast]]))

(def nativebase-provider (reagent/adapt-react-class NativeBaseProvider))

(def box (reagent/adapt-react-class Box))

(def container (reagent/adapt-react-class Container))

(def heading (reagent/adapt-react-class Heading))

(def text (reagent/adapt-react-class Text))

(def button (reagent/adapt-react-class Button))

(def spacer (reagent/adapt-react-class Spacer))

(def pressable (reagent/adapt-react-class Pressable))

(def flex (reagent/adapt-react-class Flex))

(def hstack (reagent/adapt-react-class HStack))
(def vstack (reagent/adapt-react-class VStack))
(def zstack (reagent/adapt-react-class ZStack))

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
     [pressable
      {:onPress (fn []
                  (j/call toast
                       ; :show (toast-render props "Hello! Have a nice day")))}
                           :show (toast-render props "ᠠᠰᠠᠭᠤᠳᠠᠯ ᠲᠠᠢ ᠠᠩᠬᠠᠷ")))}

      [box {:bg "emerald.500" :p "2" :rounded "sm"}
       ; [text/text-view (merge {:text "ᠰᠢᠯᠭᠠᠵᠤ ᠦᠵᠡᠶ᠎ᠡ"
                               ; :height "auto"
                            ; (bean/->clj props)}]]]))
       "Custom Toast"]]]))

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


(defn view11 []
  [center {:flex 1 :px 3 :safeArea true}
   [box {:bg "emerald.500" :px "2" :py "1" :rounded "sm" :mb 5}
    [text "abc"]]])

(defn text-view [props text]
  (let [[props _] (useStyledSystemPropsResolver (bean/->js props))]
    [text/text-view (merge {:text text}
                            ; :height "auto"}
                         (bean/->clj props))]))

(defn view []
  [center {:flex 1 :py 3 :safeArea true}
   [pressable
    (fn [x]
      (let [{:keys [isHovered isFocused isPressed]} (j/lookup x)]
        (reagent/as-element
         [box {:bg (cond
                     isPressed "cyan.900"
                     isHovered "cyan.800"
                     :else "cyan.700")
               :p "5"
               :rounded "8"
               :style {:transform [{:scale (if isPressed 0.96 1)}]}}
           ; [hstack {:alignItems "flex-start"}]
            ; [text {:fontSize 12 :color "cyan.50" :fontWeight "medium"} "Business"]
            ; [text {:fontSize 10 :color "cyan.100"} "1 month ago"]]]])))]])
          [hstack {:alignItems "flex-start"}
           [vstack
            [:f> text-view {:height "auto" :fontSize 12 :color "cyan.50" :fontWeight "medium"} "Business"]
            [spacer]
            [:f> text-view {:height "auto" :fontSize 10 :color "cyan.100"} "1 month ago"]]
           [:f> text-view {:height "auto" :width 400 :color "cyan.50" :ml "3" :fontWeight "medium" :fontSize 20}
              "Marketing License"]
           [spacer]
           [:f> text-view {:height "auto" :ml "2" :fontSize 14 :color "cyan.100"}
            "Unlock powerfull time-saving tools for"]
           [spacer]
           [:f> text-view {:height "auto" :ml "2" :fontSize 14 :color "cyan.100"}
            "creating email delivery and collecting"]
           [spacer]
           [:f> text-view {:height "auto" :ml "2" :fontSize 14 :color "cyan.100"}
            "marketing data"]
           [flex
            (if isFocused
              [:f> text-view {:height "auto"
                              :ml "2"
                              :fontSize 12
                              :fontWeight "medium"
                              :bg "cyan.500"
                              :color "cyan.200"
                              :alignSelf "flex-start"}
                "Read More"]
              [:f> text-view {:height "auto" :ml "2"
                              :fontSize 12 :fontWeight "medium" :color "cyan.400"}
               "Read More"])]]])))]])
           ; [text {:color "cyan.50" :mt "3" :fontWeight "medium" :fontSize 20}
           ;  "Marketing License"]
           ; [text {:mt "2" :fontSize 14 :color "cyan.100"}
           ;  "Unlock powerfull time-saving tools for creating email delivery and collecting marketing data"]
           ; [flex
           ;  (if isFocused
           ;    [text {:mt "2"
           ;           :fontSize 12
           ;           :fontWeight "medium"
           ;           :bg "cyan.500"
           ;           :color "cyan.200"
           ;           :alignSelf "flex-start"}
           ;       "Read More"]
           ;    [text {:mt "2" :fontSize 12 :fontWeight "medium" :color "cyan.400"}
           ;     "Read More"])]]])))]])
