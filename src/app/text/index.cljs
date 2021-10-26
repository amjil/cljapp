(ns app.text.index
  (:require
   [reagent.core :as reagent]
   [app.font.base :as font]
   [clojure.string :as str]
   [steroid.rn.components.list :as rn-list]
   [steroid.rn.core :as rn]
   [steroid.rn.components.touchable :as touchable]
   [steroid.rn.navigation.safe-area :as safe-area]

   ["react-native-svg" :as svg]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [applied-science.js-interop :as j]))

(defn in-current-line? [runs h y space-length]
  (let [runs-length (apply + (map :width runs))]
    (>= (- h y) (+ space-length runs-length))))

(defn into-line [runs line num y]
  "line y is atom"
  (for [run runs]
    (let [width (:width run)
          run-y (assoc run :y @y :line @num)]
      (swap! line conj run-y)
      (swap! y + width))))

;; var px = evt.nativeEvent.localtionX / PixelRatio.get();
;; if one word longer then height
(defn is-shape-char? [c]
  (cond
    (< 6144 (int c) 6319)
    true

    ;; dagbur
    (= 8239 (int c)) true

    :else
    false))

(defn text-runs [text font size]
  (let [text-chars (->> (str/split text #"")
                        (map #(.codePointAt % 0))
                        (remove nil?)
                        (map int))]
    (loop [shape-chars []
           txt-chars text-chars
           runs []]
      (let [first-char (first txt-chars)]
        (cond
          (empty? txt-chars)
          (if-not (empty? shape-chars)
            (conj runs (font/run font size (apply str (map char shape-chars))))
            runs)

          (is-shape-char? first-char)
          (recur (conj shape-chars first-char)
                 (rest txt-chars)
                 runs)

          (not-empty shape-chars)
          (recur []
                 (rest txt-chars)
                 (conj runs
                       (font/run font size (apply str (map char shape-chars)))
                       (font/run font size (str (char first-char)))))

          :else
          (recur shape-chars
                 (rest txt-chars)
                 (conj runs (font/run font size (str (char first-char))))))))))

(defn text-component [{:keys [height font font-size]} text]
  (let [line                     (reagent/atom [])
        line-num                 (reagent/atom 0)
        y                        (reagent/atom 0)
        [space-width line-width] (font/space-dimention :white 24)
        text-runs                (text-runs text font font-size)
        new-line-fn              (fn [] (swap! line-num inc)
                                        (reset! line [])
                                        (reset! y 0))]
    (loop [lines    []
           run-runs text-runs]
      (let [runs (first run-runs)]
        (cond
          (empty? run-runs)
          (conj lines @line)

          ;; return
          (= [10] (-> runs first :code-points))
          (let [item (conj @line (map #(assoc % :svg "" :y @y :line @line-num) runs))]
            (new-line-fn)
            (recur (conj lines (flatten item))
                   (rest run-runs)))

          ;; line start with space
          (and (= [32] (-> runs first :code-points)) (= @y 0))
          (do
            (swap! line conj runs)
            (recur lines (rest run-runs)))

          (in-current-line? runs height @y space-width)
          (do
            (doall (into-line runs line line-num y))
            (recur lines (rest run-runs)))

          :else
          (let [item @line]
            (new-line-fn)
            (recur (conj lines (flatten item))
                   run-runs)))))))



(defn text-area [props text-svgs]
  (let [line-height (:line-height props)
        half-line-h (/ line-height 2)]
    (if (font/get-font :white)
      [:> svg/Svg (merge {:width  "100%"
                          :height "100%"}
                         props)
       (doall
         (map-indexed
          (fn [idx item]
            (doall
              (map-indexed
               (fn [i run]
                 (if-not (empty? (:svg run))
                    ;; default is padding left
                   (let [x (str (+ half-line-h (* idx line-height)))]
                     [:> svg/Path {:d        (:svg run)
                                   :x        x
                                   :y        (str (:y run))
                                   :rotation "90"
                                   :key      (str idx "-" i)}])))
               item)))
          text-svgs))])))

(defn text-line [props svgs]
  [:> svg/Svg (merge {:height "100%"
                      :fill "black"}
                     props
                     {:width (str (:width props))})

   (doall
     (map-indexed
       (fn [i run]
         (if-not (empty? (:svg run))
           [:> svg/Path {:d        (:svg run)
                         :x        (/ (:width props) 2)
                         :y        (str (:y run))
                         :rotation "90"
                         :key      (str i)}]))
       svgs))])

(defn flat-list-item [props svgs]
  [touchable/touchable-opacity {}
   [rn/view {:style {}} ;{:padding-horizontal 30}} ;:margin-vertical 10}}
    ;; TODO if no text tag svg can't render, test later
    ; [rn/text {} ""]
    [text-line props svgs]]])

(defn flat-list-text [props text-svgs]
  (if (font/get-font :white)
    [rn-list/flat-list
     {:key-fn    identity
      :data      text-svgs
      ; :render-fn text-list-item
      :render-fn (partial flat-list-item props)
      :horizontal true
      :onScrollBeginDrag #(js/console.log "on scroll begin >>>")
      :onScrollEndDrag #(js/console.log "on scroll end >>>")}]))

(defn cursor-location [evt line-height svgs]
  (let [ex (j/get evt :x)
        ey (j/get evt :y)

        ; _ (js/console.log "xxx1111")
        x (loop [i      0]
            (cond
              (empty? svgs)
              0

              (<= ex (+ (* i line-height) line-height))
              i

              (>= i (count svgs))
              (if (zero? (count svgs))
                0
                (dec i))

              (= (inc i) (count svgs))
              i

              :else
              (recur (inc i))))
        ; _ (js/console.log "xxx1112 = " x)

        line (nth svgs x)
        item-count (count line)

        ; _ (js/console.log "xxx >>> " item-count)
        y (loop [i      0]
            ; (js/console.log "xxx111>> " i)
            (if (= item-count i)
              (let [item (last line)]
                (+ (:y item) (:width item)))

              (let [item (nth line i)
                    item-y (+ (:y item) (:width item))]
                (cond
                  (<= ey item-y)
                  item-y

                  :else
                  (recur (inc i))))))]
        ; _ (js/console.log "xxx1113")]
    ; [(+ x 8) y]
    [(+ (* line-height x) 8) y]))



;; (def inner-size 0)
(def font (font/get-font :white))
(def word font/mstr)
;; (require '[cljs-bean.core :refer [bean ->clj ->js]])
;; (def glyphs     (-> font
;;                     (j/call :layout word)
;;                     (j/get :glyphs)
;;                     (->clj)))
;; (def glyph (first glyphs))
; (def runs (font/run :white 24 font/mstr))
(comment
  (prn 'Hi)
  font/fonts
  (js/console.log (:white @font/fonts))
  (is-shape-char? (first (seq font/mlongstr)))
  ;; :f
  (int (first (seq font/mlongstr)))
  (js/console.log (seq font/mlongstr))
  (map #(.codePointAt (char %) 0) (seq font/mlongstr))
  (map #(int (.codePointAt % 0)) (str/split font/mlongstr #""))
  (apply str (map char [6196 6176 6192 6180 6194 6180]))
  (is-shape-char? \space)
  (text-runs font/mlongstr :white 24)
  (nth (text-component 0 500 :white 24 font/mlongstr) 0)
  (text-component 0 500 :white 24 font/mlongstr)
  (map #(dissoc % :svg) ( first (text-component 0 500 :white 24 font/mlongstr)))
  (font/space-dimention :white 24))
