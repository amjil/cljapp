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
   [steroid.rn.components.ui :as rn-ui]
   [app.ui.components :as ui]
   [steroid.rn.navigation.stack :as stack]
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [app.text.index :as text]
   [app.font.base :as font]
   [app.handler.gesture :as gesture]

   ["react-native-smooth-blink-view" :default blinkview]
   ["react-native-svg" :as svg]
   ["react-native-linear-gradient" :default linear-gradient]

   [app.ui.home.views :as home]
   [app.ui.profile.views :as profile]
   [app.ui.modal.views :as modal]
   [app.ui.toast.views :as toast]
   [app.ui.drawer.index :as drawer]
   [app.ui.nativebase :as nativebase]
   [app.ui.user.login :as login]
   [app.ui.webview :as webview]
   [app.ui.editor :as editor]
   [app.ui.article.index :as article]
   [app.ui.question.index :as question]))


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

(defn home []
  (let [h (reagent/atom nil)]
    (fn []
      [rn/view {:style {:height "100%" :width "100%"}
                :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                              (reset! h height))}])))

(defn edit-view []
  [nativebase/center {:flex 1 :px 3 :safeArea true}
   [nativebase/text "edit-view"]])

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
    ; {:name      :book
    ;  ; :component webview/webview-editor
    ;  :component article/main}
    ; {:name      :book
    ;  :component edit-view}
    (merge
      question/question-list
      {:name :book})
    (merge
      article/article-list
      {:name :edit})
    {:name      :profile
     :component profile/profile}]])

(defn root-stack []
  [safe-area/safe-area-provider
   [(rnn/create-navigation-container-reload                 ;; navigation container with shadow-cljs hot reload
     {:on-ready #(re-frame/dispatch [:initialise-app])}     ;; when navigation initialized and mounted initialize the app
     [nativebase/nativebase-provider {:config {:dependencies {"linear-gradient" linear-gradient}}}
      [stack/stack {}
       [{:name      :main
         :component tabs
         :options {:title ""}}
        article/article-detail
        article/article-edit
        article/article-list
        question/question-list
        question/question-detail
        question/question-edit
        profile/profile-edit]]])]])
     ;   {:name       :keyboard
     ;    :component  keyboard/keyboard-view
     ;    :options    {:title ""}}
     ;   {:name       :toast
     ;    :component  toast/view
     ;    :options    {:title ""}}]])]])
      ; [:f> nativebase/view]])]])
      ; [drawer/view]])]])
      ; [login/view]])]])
      ; [webview/view]])]])
; (defn root-stack []
;   [safe-area/safe-area-provider
;    [nativebase/nativebase-provider
;     ; [nativebase/view]
;     [nativebase/box {:flex 1 :px 3 :safeArea true}
;     ;  [webview/webview-editor]]]])
;     ; [editor/view]]])
;     ; [editor/test-view]]])
;      [home/home]]]])
