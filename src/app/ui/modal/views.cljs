(ns app.ui.modal.views
  (:require
    [reagent.core :as reagent]
    [steroid.rn.core :as rn]
    [steroid.rn.components.other :as rn-other]
    [steroid.rn.components.touchable :as touchable]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [app.text.index :as text]))

(def is-visible (reagent/atom false))

(defn modal [items]
  (fn []
    [rn-other/modal {:isVisible @is-visible
                     :style {:margin 0
                             :justifyContent "flex-end"}}
     [rn/view {:style {:borderTopLeftRadius 12
                       :borderTopRightRadius 12
                       :borderBottomLeftRadius 12
                       :borderBottomRightRadius 12
                       :marginBottom 8
                       :marginTop 8
                       :marginRight 20}}
      (doall
        (map-indexed [idx item]
          [touchable/touchable-highlight
           {:style (merge {:backgroundColor "#ffffff"
                           :display "flex"
                           :flexDirection "column"
                           :justifyContent "center"
                           :alignItems "center"
                           :paddingLeft 16
                           :paddingRight 16
                           ; :borderRightWidth StyleSheet.hairlineWidth,
                           :borderRightWidth 1,
                           :borderColor "#DBDBDB"}
                          (when (= 0 idx)
                            {:borderTopLeftRadius 12
                             :borderBottomLeftRadius 12})
                          (when (= (- (count items) 2) idx)
                            {:borderTopRightRadius: 12,
                             :borderBottomRightRadius: 12,})
                          (when (= (dec (count items)) idx)
                            {:borderRightWidth 0
                             :backgroundColor "#ffffff"
                             :marginLeft 8
                             :borderTopLeftRadius 12
                             :borderTopRightRadius 12
                             :borderBottomLeftRadius 12
                             :borderBottomRightRadius 12}))
            :underlayColor "#f7f7f7"
            :key idx
            :onPress #(do (js/console.log "pressed >>>> " idx))}
           [text/text-line {:width 42}
            (first (text/text-component
                     {:width 0 :fill (if (= idx (dec (count items))) "#fa1616" "rgb(0,98,255)")
                      :height 400 :font :white :font-size 18}
                     (:label item)))]]
          items))]]))

(defn )
