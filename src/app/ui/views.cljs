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
   [app.ui.components :as ui]
   [steroid.rn.navigation.stack :as stack]
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]

   [app.text.index :as text]
   [app.font.base :as font]
   [app.components.gesture :as gesture]

   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]))


(when platform/android?
  (status-bar/set-bar-style "dark-content")
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
    [rn/text {:style {:backgroundColor "green"}} "Hello CLojure! from CLJS "]])

(defn text-editor [height text]
  (let [text-svgs (text/text-component {:width 32 :fill "gray" :color "black" :height height :font :white :font-size 24} text)
        runs (last (last text-svgs))
        x (reagent/atom nil)
        y (reagent/atom nil)]
    (reset! y (:y runs))
    (reset! x (+ 8 (* 32 (dec (count text-svgs)))))
    (fn []
      [gesture/tap-gesture-handler
       {:onHandlerStateChange #(if (gesture/tap-state-end (j/get % :nativeEvent))
                                   (let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                     (js/console.log "x = " ex " y = " ey)
                                     (reset! x ex)
                                     (reset! y ey)))}
       [gesture/pan-gesture-handler {:onGestureEvent #(let [[ex ey] (text/cursor-location (j/get % :nativeEvent) 32 text-svgs)]
                                                        (js/console.log "x = " ex " y = " ey))}
        [rn/view {:style {:height "100%" :width "100%"}}
         [:> blinkview {"useNativeDriver" false}
          [rn/view {:style {:position :absolute :top (or @y 0) :left (or @x 0)}}
           [:> svg/Svg {:width 32 :height 2}
            [:> svg/Rect {:x "0" :y "0" :width 32 :height 2 :fill "black"}]]]]
         [text/flat-list-text {:width 32 :fill "gray" :color "black" :height height :font :white :font-size 24} text-svgs]]]])))

(defn home []
  (let [h (reagent/atom nil)]
    (fn []
      [rn/view {:style {:height "100%" :width "100%"}
                :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                              (reset! h height))}
       (when @h
         [text-editor @h font/mlongstr])])))


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
     :component home}
    {:name      :book
     :component component}
    {:name      :edit
     :component component}
    {:name      :profile
     :component component}]])

(defn root-stack []
  [safe-area/safe-area-provider
   [(rnn/create-navigation-container-reload                 ;; navigation container with shadow-cljs hot reload
     {:on-ready #(re-frame/dispatch [:initialise-app])}     ;; when navigation initialized and mounted initialize the app
     [stack/stack {}
      [{:name      :main
        :component tabs
        :options {:title "cljapp"}}]])]])
