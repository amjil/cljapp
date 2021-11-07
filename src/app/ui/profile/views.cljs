(ns app.ui.profile.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as reagent]
    [app.text.index :as text]
    [steroid.rn.core :as rn]
    [steroid.rn.components.touchable :as touchable]
    [steroid.rn.components.list :as rn-list]
    [steroid.rn.components.other :as rn-other]))


(defn profile-flat-item [props item]
  (if (:text item)
    [touchable/touchable-highlight {:underlayColor "#DDDDDD"
                                    :activeOpacity 0.6
                                    :onPress #(re-frame/dispatch [:navigate-to :keyboard])}
     [rn/view {:style {:width 52 :height "100%" :padding-right 20}} ;{:padding-horizontal 30}} ;:margin-vertical 10}}
      [text/text-line props (first (text/text-component {:width 0 :fill "gray" :color "black" :height 400 :font :white :font-size 24} (:text item)))]]]
    [rn/view {:style {:width 10 :backgroundColor "lightgrey"}}]))

(defn profile []
 [rn-list/flat-list
  {:key-fn    identity
   :data      [{:key 0} {:text "ᠳᠡᠪᠢᠰᠭᠡᠷ"} {:text "ᠳᠠᠭᠠᠪᠤᠷᠢ"} {:text "abc"} {:key 1} {:text "mgl"} {:key 2}]
   :render-fn (partial profile-flat-item {:width 42})
   :horizontal true
   :showsVerticalScrollIndicator false
   :contentContainerStyle {:flexGrow 1 :alignSelf "center" :flexDirection "row"}
   :refreshControl (reagent/as-element
                     [rn-other/refresh-control
                       {:tintColor "black"
                        :refreshing false
                        :onRefresh #(do (js/console.log "on Refresh >>>")
                                      true)}])

   :ItemSeparatorComponent
   (fn [] (reagent/as-element [rn/view {:style {:width 1 :backgroundColor "lightgrey"}}]))}])
