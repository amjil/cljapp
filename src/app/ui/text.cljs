(ns app.ui.text
  (:require
    [app.ui.nativebase :as nbase]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    ["react-native-measure-text-chars" :as rntext]
    ["native-base" :refer [useThemeProps useStyledSystemPropsResolver]]

    [steroid.rn.core :as rn]))

(defn rotated-text [props width height t]
  (let [offset (- (/ height 2) (/ width 2))]
    [rn/text {:style (merge {:width height :height width
                             :transform [{:rotate "90deg"}
                                         {:translateX offset}
                                         {:translateY offset}]}
                            props)}
      t]))

(defn measured-text
  [props t]
  (let [info (rntext/measure (bean/->js (assoc props :text (if (empty? t) "A" t))))
        height (j/get info :width)
        width (+ 1 (j/get info :height))]
    [rn/view {:style {:width width
                      :height height}}
     [rotated-text props width height (if (empty? t) "" t)]]))

(defn theme-text-props [name props]
  (let [theme-props (bean/->js (useThemeProps name (bean/->js props)))
        [text-props _] (useStyledSystemPropsResolver (bean/->js (j/get theme-props :_text)))]
    text-props))

(defn theme-props [name props]
  (let [theme-props (bean/->js (useThemeProps name (bean/->js props)))
        [text-props _] (useStyledSystemPropsResolver (bean/->js theme-props))]
    [(bean/->clj theme-props) (bean/->clj text-props)]))

(defn styled-text-view [props t]
  (let [[text-props _] (useStyledSystemPropsResolver (bean/->js props))]
    [measured-text (bean/->clj text-props) t]))
