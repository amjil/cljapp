(ns app.ui.article.new
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]
    [app.text.message :refer [labels]]

    [steroid.rn.core :as rn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(defn title-view [on-edit focus-elem]
  [rn/touchable-opacity {:onPress (fn [] (js/console.log "touchable without feedback .....")
                                    (reset! focus-elem 1))}
   [text/measured-text {:fontSize 22 :color "#71717a" :fontFamily "MongolianBaiZheng"} (get-in labels [:question :title-placeholder])]])


(defn view []
  (let [on-edit (reagent/atom false)
        h (reagent/atom nil)
        focus-elem (reagent/atom 0)]
    (fn []
      [nbase/box
       {:on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                      (reset! h height))
        :flex 1}
       (if-not (nil? @h)
         [nbase/flex {:flex 1 :p 5 :flex-direction "row" :bg "white"}
          (if (= 1 @focus-elem)
            [editor/editor-view
             ; opts
             {:placeholder (get-in labels [:question :title-placeholder])
              :type :text}
             ;content-fn
             (fn []
               ; (js/console.log "content -fn >......."))
               "")
               ; (get @model @active-key))
             ;update-fn
             (fn [x] (js/console.log "1112"))]
            [title-view on-edit focus-elem])
          [nbase/divider {:orientation "vertical" :mr "3" :ml "1"}]
          [rn/touchable-opacity {:onPress #(js/console.log "touchable without feedback .....2")}
           [text/measured-text {:fontSize 18 :color "#71717a" :fontFamily "MongolianBaiZheng" :width @h} (get-in labels [:question :content-placeholder])]]])])))

    ; [editor/editor-view
    ;  ; opts
     ; {:placeholder (get-in labels [:question :title-placeholder])
     ;  :type :text}
     ; ;content-fn
     ; (fn []
     ;   ; (js/console.log "content -fn >......."))
     ;   "")
     ;   ; (get @model @active-key))
     ; ;update-fn
     ; (fn [x] (js/console.log "1112")))))

(def model-new
  {:name       :model-new
   :component  view
   :options
   {:title ""}})
