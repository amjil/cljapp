(ns app.ui.picker.views
  (:require
    ["react-native-smooth-picker" :default smoothpicker]
    [reagent.core :as reagent]
    [steroid.rn.core :as rn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [app.text.index :as text]))

(def opacities
  {0 1
   1 1
   2 0.6
   3 0.3
   4 0.1})

(def size-text
  {0 20
   1 15
   2 10})

(defn render-item [selected option]
  (let [{:keys [item index]} (j/lookup option)
        gap (js/Math.abs (- index @selected))
        opacity (cond
                  (> gap 3)
                  (get opacities 4)

                  :else
                  (get opacities gap))
        font-size (cond
                    (> gap 1)
                    (get size-text 2)

                    :else
                    (get size-text gap))]
    (reagent/as-element
      [rn/view (merge {    :justifyContent "center"
                           :alignItems "center"
                           :marginTop 10
                           :marginBottom 10
                           :paddingTop 30
                           :paddingBottom 30
                           :paddingLeft 10
                           :paddingRight 10
                           :height 300
                           :borderWidth 3
                           :borderRadius 10}
                    {:opacity opacity
                     :borderColor (if (= index @selected)
                                    "#ABC9AF" "transparent")
                     :width "auto"})
        [text/text-line {:width 42} (first (text/text-component {:width 0 :fill "gray" :color "black" :height 400 :font :white :font-size font-size} item))]])))


(defn picker []
  (let [selected (reagent/atom 4)]
    (fn []
      [rn/view {:style {:height 400
                        :justifyContent "center"
                        :alignItems "center"
                        :margin "auto"
                        :color "black"}}
       [:> smoothpicker {:offsetSelection 4
                         :scrollAnimation true
                         :data ["ᠨᠢᠭᠡ" "ᠬᠤᠶᠠᠷ" "ᠭᠤᠷᠪᠠ" "ᠳᠦᠷᠪᠡ" "ᠲᠠᠪᠤ" "ᠵᠢᠷᠭᠤᠭ᠎ᠠ" "ᠳᠣᠯᠣᠭ᠎ᠠ" "ᠨᠠᠢᠮᠠ" "ᠶᠢᠰᠦ" "ᠠᠷᠪᠠ"]
                         :onSelected (fn [option]
                                       (let [{:keys [item index]} (j/lookup option)]
                                         (reset! selected index)))
                         :horizontal true
                         :showsHorizontalScrollIndicator false
                         :renderItem (partial render-item selected)}]])))
