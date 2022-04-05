(ns app.ui.article.index
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]

    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [app.ui.html :as html]
    ["react-native-webview" :refer [WebView]]
    ["native-base" :refer [useStyledSystemPropsResolver]]

    ["react-native-measure-text-chars" :as rntext]
    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(defn rotated-text [props width height t]
  (let [offset (- (/ height 2) (/ width 2))]
    [nbase/text (merge props {:style {:width height :height width
                                      :transform [{:rotate "90deg"}
                                                  {:translateX offset}
                                                  {:translateY offset}]}})
      t]))

(defn measured-text
  [props t]
  (let [[text-props _] (useStyledSystemPropsResolver (bean/->js props))
        text-props (bean/->clj text-props)
        info (rntext/measure (bean/->js (assoc text-props :text (if (empty? t) "A" t))))
        height (j/get info :width)
        width (+ 1 (j/get info :height))]
    [nbase/box {:style {:width width
                        :height height}}
     [rotated-text text-props width height (if (empty? t) "" t)]]))

; (def model (reagent/atom {:title "Hello" :content "<p>abc</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>xxxx</p>"}))
(def model (reagent/atom {:title "Hello" :content "<p>abc</p><p>xxxx</p>"}))
(def active-key (reagent/atom nil))
(def content-type (reagent/atom nil))

(defn view []
  [ui/safe-area-consumer
   [nbase/flex {:flex 1 ;:safeArea true
                :p 5
                :flex-direction "row"
                :bg "white"}
    [nbase/box {:p 2}
     [:f> measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠭᠠᠷᠴᠠᠭ"]]
    [nbase/pressable {:border-width "1" :border-color "cyan.500"
                      :px 2
                      :py 5
                      :border-radius "md"
                      :on-press (fn [] (reset! active-key :title)
                                  (reset! content-type :text)
                                  (re-frame/dispatch [:navigate-to :article-edit]))}
     [:f> measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} (:title @model)]]
    [nbase/box {:p 2 :ml 2}
     [:f> measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠠᠭᠤᠯᠭ᠎ᠠ"]]
    [nbase/box {:border-width "1" :border-color "cyan.500"
                :w "200"
                :p 2
                :max-w "50%"
                :border-radius "md"
                :on-press (fn [] (reset! active-key :content))}
     [editor/simple-view
      ; (:content @model)
      (fn [] (:content @model))
      ; "<p>abc</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>def</p><p>xxxx</p>"
      (fn [] (reset! active-key :content)
        (reset! content-type :content)
        (re-frame/dispatch [:navigate-to :article-edit]))]]]])

(def articles-atom (reagent/atom [{:title "abc" :content "<p>abc</p>"}
                                  {:title "hello" :content "<p>hello world</p>"}]))

(def articles-cursor (reagent/atom 0))

(defn edit-view []
  [ui/safe-area-consumer
   [nbase/flex {:flex 1
                :justifyContent "space-between"}
    [editor/editor-view
      ;content-fn
      (fn []
        (js/console.log "content -fn >......." (:content @model))
        (get @model @active-key))
      ;update-fn
      (fn [x]
        (swap! model assoc @active-key (get x @content-type))
        (swap! articles-atom assoc @articles-cursor @model)
        (js/console.log "change-fn>>> " (:content x)))]
    [candidates/views]
    [nbase/box {:height 220}
     [keyboard/keyboard]]]])


(defn list-view []
  [ui/safe-area-consumer
   [nbase/flat-list
    {:keyExtractor    (fn [_ index] (str "item-view-" index))
     :data      @articles-atom
     :renderItem (fn [x]
                   (let [{:keys [item index separators]} (j/lookup x)]
                     (reagent/as-element
                       [nbase/pressable {:style {:height "100%"}; :width 28}}
                                         :on-press (fn [e] (re-frame/dispatch [:navigate-to :article-detail])
                                                     (reset! articles-cursor index)
                                                     (reset! model (bean/->clj item)))}
                        [:f> measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18}
                          (j/get item :title)]])))
     :ItemSeparatorComponent
     (fn [] (reagent/as-element [nbase/divider {:bg "emerald.500" :thickness "2" :orientation "vertical" :mx "2"}]))
     :p 3
     :initialNumToRender 7
     :showsHorizontalScrollIndicator false
     :horizontal true}]])

(defn main []
  [ui/safe-area-consumer
   ; [nbase/button {:on-press #(re-frame/dispatch [:navigate-to :article-detail])}  "up"]
   [nbase/button {:on-press #(re-frame/dispatch [:navigate-to :article-list])}  "up"]])

(def article-detail
  {:name       :article-detail
   :component  view
   :options
   {:title ""
    :headerRight
    (fn [tag id classname]
      (reagent/as-element
        [nbase/icon-button {:variant "ghost" :colorScheme "indigo"
                            :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-checkmark"}])
                            :on-press (fn [e] (js/console.log "on press icon button"))}]))}})

(def article-edit
  {:name       :article-edit
   :component  edit-view
   :options
   {:title ""
    :headerRight
    (fn [tag id classname]
      (reagent/as-element
        [nbase/icon-button {:variant "ghost" :colorScheme "indigo"
                            :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-checkmark"}])
                            :on-press #(js/console.log "on press icon button")}]))}})

(def article-list
  {:name       :article-list
   :component  list-view
   :options
   {:title ""
    :headerRight
    (fn [tag id classname]
      (reagent/as-element
        [nbase/icon-button {:variant "ghost" :colorScheme "indigo"
                            :icon (reagent/as-element [nbase/icon {:as Ionicons :name "ios-add"}])
                            :on-press (fn [e] (js/console.log "on press icon button")
                                        (reset! articles-cursor (count @articles-atom))
                                        (reset! model {:title "" :content ""})
                                        (re-frame/dispatch [:navigate-to :article-detail]))}]))}})
