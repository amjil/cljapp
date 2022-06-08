(ns app.ui.search
  (:require
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.text :as text]
    [app.ui.editor :refer [weblen]]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    [app.ui.keyboard.bridge :as bridge]
    [app.ui.basic.theme :as theme]
    [app.text.message :refer [labels]]

    [steroid.rn.core :as rn]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]

    ["react-native-modal" :default rnmodal]
    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(defn base-view []
  (let [h (reagent/atom nil)]
    (fn []
      [nbase/box {:flex 1 :bg (theme/color "white" "dark.100")}
       [nbase/zstack {:flex 1
                      :bg (theme/color "white" "dark.100")
                      :on-layout #(let [height (j/get-in % [:nativeEvent :layout :height])]
                                    (reset! h height))}
        (if-not (nil? @h)
          [nbase/box {:flex 1 :bg (theme/color "coolGray.300" "coolGray.500")
                      :p 1}
           [nbase/box {:style {:height (- @h 4)} :bg (theme/color "white" "dark.100")
                       :borderRadius 4
                       :minWidth 10
                       :maxWidth 24}
            [editor/editor-view
             {:type :text}
             ;content-fn
             (fn []
               ; (js/console.log "content -fn >......." (:content @model))
               ; (get @model @active-key))
               "")
             ;update-fn
             (fn [x] (js/console.log "editir update-fn"))]]])
               ; (swap! messages
                  ; assoc @conversation-name (concat [{:me true :message (:text x)}] (get @messages @conversation-name))])]]])

        [candidates/views {:bottom 20}]]
       [nbase/box {:height 220 :mt 1}
        [keyboard/keyboard {:type "single-line"
                            :on-press (fn []
                                        (bridge/editor-content)
                                        (candidates/candidates-clear))}]]])))


(def search-base
  {:name       :search-base
   :component  base-view
   :options
   {:title ""
    :headerShown true}})
