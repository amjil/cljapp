(ns app.ui.profile.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as reagent]
    [applied-science.js-interop :as j]
    [cljs-bean.core :as bean]
    [app.ui.text :as text]
    [app.ui.nativebase :as nbase]
    [app.ui.editor :as editor]
    [app.ui.components :as ui]
    [app.ui.keyboard.index :as keyboard]
    [app.ui.keyboard.candidates :as candidates]
    ["react-native-vector-icons/Ionicons" :default Ionicons]))

(def profiles [{:name "ᠨᠡᠷ᠎ᠡ" :value "ᠰᠡᠴᠡᠨᠪᠦᠬᠡ"}
               {:name "ᠬᠦᠢᠰᠦ" :value "ᠡᠷᠡᠭᠲᠡᠢ"}
               {:name "ᠲᠦᠷᠦᠭᠰᠡᠨ ᠡᠳᠦᠷ" :value "2020-01-01"}
               {:name "ᠢᠨᠠᠭ" :value "ᠤᠷᠤᠭᠯᠠᠪᠠ"}
               {:name "ᠳᠤᠷ᠎ᠠ ᠪᠠᠬ᠎ᠠ" :value "ᠬᠦᠯ ᠪᠦᠮᠪᠦᠭᠡ"}
               {:name "ᠨᠤᠲᠤᠭ" :value "ᠵᠠᠷᠤᠳ"}
               {:name "ᠠᠵᠢᠯ" :value "IT"}
               {:name "ᠲᠠᠨᠢᠯᠴᠠᠭᠤᠯᠭ᠎ᠠ" :value ""}
               {:name "ᠦᠪᠡᠷᠮᠢᠴᠡ ᠦᠬᠡ" :value ""}])

(defn profile []
  [nbase/hstack {:bg "white" :h "100%"}
   [nbase/vstack {:h "100%"
                  :mx 4
                  :px 2
                  :borderLeftWidth "1"
                  :borderRightWidth "1"
                  :borderColor "gray.200"}
    [nbase/box {:mt 2 :p "6" :borderRadius "md" :bg "primary.200"}]
    [nbase/box {:mt 2
                :ml 1
                :justifyContent "center" :alignSelf "center" :alignItems "center"}
     [text/measured-text {:fontSize 18 :fontFamily "MongolianBaiZheng"} "ᠰᠡᠴᠡᠨᠪᠦᠬᠡ"]]
    [nbase/flex {:justifyContent "flex-end"
                 :flex 1}
     [nbase/box {:p "4" :bg "primary.200"
                 :mb 2}]
     [nbase/icon {:as Ionicons :name "chevron-down"
                  :justifyContent "center" :alignSelf "center"}]]]
   [nbase/flat-list
    {:keyExtractor    (fn [_ index] (str "profile-item-" index))
     :data      profiles
     :renderItem (fn [x]
                   (let [{:keys [item index separators]} (j/lookup x)]
                     (reagent/as-element
                       [nbase/pressable {:style {:height "100%"}; :width 28}}
                                         :borderLeftWidth "1"
                                         :borderColor "gray.200"
                                         :on-press (fn [e]
                                                     (js/console.log "pressable on press >>>"))}
                        [nbase/box {:h "20%"
                                    :mx 2
                                    :pl 2
                                    :pt 4}
                         [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18
                                              :color "gray.500"}
                           (j/get item :name)]]
                        [nbase/divider {:bg "gray.200" :thickness "1"
                                        :w "100%"}]
                        [nbase/box {:mx 2
                                    :pl 2
                                    :pt 4}
                         [text/measured-text {:fontFamily "MongolianBaiZheng" :fontSize 18
                                              :color "gray.500"}
                           (j/get item :value)]]])))

     :w "auto"
     :ml 2
     :px 2
     :horizontal true}]])
