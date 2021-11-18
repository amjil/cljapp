(ns app.font.base
  (:require
   ["@pdf-lib/fontkit" :as fontkit]
   ["react-native-fs" :as fs]
   ["react-native" :as rn]
   [goog.crypt.base64 :as b64]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->clj ->js]]
   [promesa.core :as p]
   [reagent.core :as reagent]))

;; (defonce ^:private
(def
  fonts
  (reagent/atom {}))

(defn init []
  (js/console.log "init func started...")
  (p/let [result (let [platform (j/get-in rn [:Platform :OS])]
                   (condp = platform
                     "android" (.readFileAssets fs "mnglwhiteotf.ttf" "base64")
                     "ios" (.readFile fs (str fs/MainBundlePath "/assets/mnglwhiteotf.ttf") "base64")))]
    (p/then result (fn [r]
                     (swap! fonts assoc :white (fontkit/create (b64/decodeStringToUint8Array result))))))
  (js/console.log "init function, is nil?, " (nil? (get @fonts :white))))

;; (.then (.readFileAssets fs "monbaiti.ttf") (fn [res] (swap! fonts assoc :white (fontkit/create res))))
;; (.then (.readFileAssets fs "monbaiti.ttf" "base64") (fn [res] (swap! fonts assoc :white (fontkit/create (b64/decodeStringToUint8Array res)))))

(defn get-font [name]
  (get @fonts name))

(defn character-set [name]
  (j/get (get-font name) "characterSet"))

(defn font-size [font size]
  (/ size (j/get font :unitsPerEm)))

(defn svg [glyph size]
  (if (nil? (j/get glyph :path))
    ""
    (-> glyph
        (j/get :path)
        (j/call :scale -1 1)
        (j/call :rotate (str (.-PI js/Math)))
        (j/call :scale size)
        (j/call :toSVG))))

(defn space-dimention [font-name size]
  (let [font (get-font font-name)]
    (if font
      (let [glyph (-> font
                      (j/call :layout " ")
                      (j/get :glyphs)
                      last)
            inner-size (font-size font size)]
        [(* inner-size (j/get glyph :advanceWidth))
         (* inner-size (j/get font :capHeight))]))))

(defn run [font-name size word]
  (let [font (get-font font-name)]
    (if font
      (let [inner-size (font-size font size)
            glyphs     (as-> font m
                         (j/call m :layout word)
                         (j/get m :glyphs)
                         (->clj m))]
        (map #(hash-map :svg (svg % inner-size)
                        :id (j/get % :id)
                        :width (* inner-size (j/get % :advanceWidth))
                        :code-points (->clj (j/get % :codePoints)))
             glyphs)))))

;a

(def mstr "ᠡᠷᠬᠡ")
(def mspecial " ᠢᠶᠡᠨ")
(def mlongstr "ᠴᠠᠰᠤᠲᠤ ᠬᠠᠢᠷᠬᠠᠨ ᠬᠥᠪᠴᠢ ᠬᠠᠩᠭᠠᠢ ᠡᠯᠡᠰᠦᠨ ᠮᠠᠩᠬ᠎ᠠ ᠨᠢ ᠴᠠᠭ ᠊ᠤᠨ ᠤᠷᠲᠤ ᠳᠤ ᠬᠤᠪᠢᠷᠠᠭ᠎ᠠ ᠦᠭᠡᠢ ᠡᠭᠡᠯ ᠲᠥᠷᠬᠦ ᠲᠠᠢ ᠠᠭᠤᠯᠠ ᠤᠰᠤ ᠤᠷᠭᠤᠮᠠᠯ ᠠᠮᠢᠲᠠᠨ ᠪᠤᠭᠤᠷᠤᠯ ᠳᠡᠭᠡᠳᠦᠰ ᠮᠢᠨᠢ abc ")
;; (def mlongstr "ᠴᠠᠰᠤᠲᠤ ᠬᠠᠷᠠᠬᠠᠨ ")

(comment
  (init)
  (run :white 24 "ᠠᠭᠤᠯᠠ ")
  (space-dimention :white 24)
  (font-size (get-font :white) 24)
  (-> (get-font :white)
      (j/call :layout " ")
      (j/get :glyphs)
      first
      (js/console.log))

  (map #(hash-map
        ;;  :svg (svg % 0.01171875)
         :height (* 0.01171875 (j/get % :advanceHeight))
         :width (* 0.01171875 (j/get % :advanceWidth)))
                        ;; :code-points (j/get % :codePoints)

       (-> (get-font :white)
           (j/call :layout mstr)
           (j/get :glyphs)
           (->clj)))

  fonts

  (map #(svg % :white 24) (get-glyphs :white mstr))
  (map #(svg % :white 24) (get-glyphs :white mlongstr))
  (js/console.log 
  (j/call (get-font :white) :layout mspecial "mong"))
  (js/console.log 
  (aget (.-glyphs (j/call (get-font :white) :layout mspecial)) 1))
  (.-features (j/call (get-font :white) :glyphsForString mspecial))
  (js/console.log 
(aget (j/call (get-font :white) :glyphsForString mspecial) 1))
(-> (aget (j/call (get-font :white) :glyphsForString mspecial) 0)
    (.-codePoints)
    (aget 0))
(js->clj (.-GSUB (get-font :white)))
(.keys js/Object (.-lookupList (.-GSUB (get-font :white))))
(j/get (.-GSUB (get-font :white)) :lookupList)
(js/console.log  (.-items (.-lookupList (.-GSUB (get-font :white)))))
(js->clj (.-items (.-lookupList (.-GSUB (get-font :white)))))

  
  
  )
