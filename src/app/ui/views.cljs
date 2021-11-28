(ns app.ui.views
  (:require
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean]
   [steroid.rn.core :as rn]
   [steroid.rn.navigation.core :as rnn]
   [steroid.rn.navigation.bottom-tabs :as bottom-tabs]
   [steroid.rn.components.status-bar :as status-bar]
   [steroid.rn.navigation.safe-area :as safe-area]
   [steroid.rn.components.platform :as platform]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.components.ui :as rn-ui]
   [app.ui.components :as ui]
   [steroid.rn.navigation.stack :as stack]
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [app.text.index :as text]
   [app.font.base :as font]
   [app.components.gesture :as gesture]

   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]

   [app.ui.home.views :as home]
   [app.ui.profile.views :as profile]
   [app.ui.picker.views :as picker]
   [app.ui.modal.views :as modal]
   [app.ui.keyboard.views :as keyboard]
   [app.ui.toast.views :as toast]
   [app.ui.drawer.index :as drawer]))


(when platform/android?
  (status-bar/set-bar-style "light-content")
  (status-bar/set-translucent true))


(def tab-icons
  {"home"    "md-home"
   "book"    "md-bookmark"
   "edit"    "md-create"
   "profile" "md-person"})

(defn screen-options [options]
  (let [{:keys [route]} (bean/->clj options)]
    {:tabBarIcon
     (fn [data]
       (let [{:keys [color]} (bean/->clj data)
             icon (get tab-icons (:name route))]
         (reagent/as-element
          [ui/ion-icons {:name icon :color color :size 30}])))}))

(defn component []
  [rn/view {:style {:flexDirection "column"
                    :height "100%"
                    :width "100%"}}
    [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]
    [rn-ui/button {:title "edit" :onPress #(do (reset! modal/is-visible true)
                                               (js/console.log "is-visible ----- " @modal/is-visible))}]
    [modal/modal [{:label "ᠨᠢᠭᠡ"} {:label "ᠬᠤᠶᠠᠷ"} {:label "ᠭᠤᠷᠪᠠ"}]]])

(defn component-modal []
  [rn/view {:style {:flex 1}}
   [rn/view
    [rn-ui/button {:title "edit" :onPress #(do (reset! modal/is-visible true)
                                               (js/console.log "is-visible ----- " @modal/is-visible))}]
    [modal/modal [{:label "ᠨᠢᠭᠡ"} {:label "ᠬᠤᠶᠠᠷ"} {:label "ᠭᠤᠷᠪᠠ"}]]]
   [rn/view
    [rn-ui/button {:title "toast" :onPress #(do (re-frame/dispatch [:navigate-to :toast]))}]]])




(defn home []
  (let [h (reagent/atom nil)]
    (fn []
      [rn/view {:style {:height "100%" :width "100%"}
                :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                              (reset! h height))}])))

(defn tabs []
  [bottom-tabs/bottom-tab
   {
    ; :screenOptions screen-options}
    :screenOptions
    (fn [options]
     (let [{:keys [route]} (bean/->clj options)]
       (bean/->js {:activeTintColor   "#5cb85c"
                   :inactiveTintColor :black
                   :showLabel         false
                   :tabBarLabel       (fn [] nil)
                   :headerShown       false
                   :modal             true
                   :tabBarIcon (fn [data]
                                 (let [{:keys [color]} (bean/->clj data)
                                       icon (get tab-icons (:name route))]
                                   (reagent/as-element
                                     [ui/ion-icons {:name icon :color color :size 20}])))})))}

   [{:name      :home
     :component home/home}
    {:name      :book
     :component component-modal}
    {:name      :edit
     :component picker/picker}
    {:name      :profile
     :component profile/profile}]])

(defn root-stack []
  [safe-area/safe-area-provider
   [(rnn/create-navigation-container-reload                 ;; navigation container with shadow-cljs hot reload
     {:on-ready #(re-frame/dispatch [:initialise-app])}     ;; when navigation initialized and mounted initialize the app
     ; [stack/stack {}
     ;  [{:name      :main
     ;    :component tabs
     ;    :options {:title ""}}
     ;   {:name       :keyboard
     ;    :component  keyboard/keyboard-view
     ;    :options    {:title ""}}
     ;   {:name       :toast
     ;    :component  toast/view
     ;    :options    {:title ""}}]]
     [drawer/view])]])
