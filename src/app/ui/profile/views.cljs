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
  [touchable/touchable-highlight {:underlayColor "#DDDDDD"
                                  :activeOpacity 0.6
                                  :onPress #(re-frame/dispatch [:navigate-to :keyboard])}
   [rn/view {:style {:width 52 :height "100%" :padding-right 20}}
    [text/text-inline props (:text item)]]])

(defn profile []
 [rn-list/flat-list
  {:key-fn    identity
   :data      [{:text "bbb"} {:text "aaa"} {:text "abc"}]
  ;;  :render-fn (partial profile-flat-item {:width 42})
   :render-fn (partial profile-flat-item {:width 42 :fill "black"
                                          :font :white
                                          :font-size 18})
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
