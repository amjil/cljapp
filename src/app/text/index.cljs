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
  (let [[space-width line-width] (font/space-dimention :white font-size)
        text-runs                (text-runs text font font-size)]
    (loop [lines    []
           line []
           y 0
           run-runs text-runs]
      (let [runs (first run-runs)]
        (cond
          (empty? run-runs)
          (conj lines (flatten line))

          ;; return
          (= [10] (-> runs first :code-points))
          (let [item (conj line (map #(assoc % :svg "" :y y :line (inc (count lines))) runs))]
            (recur (conj lines (flatten item))
                   []
                   0
                   (rest run-runs)))

          ;; line start with space
          (and (= [32] (-> runs first :code-points)) (= y 0))
          (recur lines
                 (conj line runs)
                 y
                 (rest run-runs))

          (in-current-line? runs height y space-width)
          (let [item-height (apply + (map :width runs))
                widths (map :width runs)
                runs-y (map-indexed #(assoc %2 :y (+ y (reduce + (take %1 widths)))) runs)]
            (recur lines
                   (conj line runs-y)
                   (+ y item-height)
                   (rest run-runs)))

          :else
          (recur (conj lines (flatten line))
                 []
                 0
                 run-runs))))))

(defn text-component-inline [{:keys [font font-size]} text]
  (let [text-runs                (text-runs text font font-size)
        [space-width line-width] (font/space-dimention font font-size)]
    (loop [y 0
           run-runs text-runs
           result-runs []]
      (let [runs (first run-runs)]
        (cond
          (empty? run-runs)
          (let [result-runs (flatten result-runs)
                last-run (last result-runs)]
            [line-width
             (+ (:width last-run) (:y last-run))
             result-runs])

          :else
          (let [item-height (apply + (map :width runs))
                widths (map :width runs)
                runs-y (map-indexed #(assoc %2 :y (+ y (reduce + (take %1 widths)))) runs)]
            (recur (+ y item-height)
                   (rest run-runs)
                   (concat result-runs runs-y))))))))

(defn draw-text [props svgs]
  [:> svg/Svg (merge {:fill "black"}
                     props)
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

(defn text-inline [props text]
  (let [[line-width height text-runs] (text-component-inline (select-keys props [:font :font-size]) text)
        props                    (merge {:fill   "white"
                                         :height height
                                         :width line-width}
                                        props)]
    [draw-text props text-runs]))

(defn flat-list-item [props svgs]
  [touchable/touchable-without-feedback {}
   [rn/view {:style {}}
    [draw-text props svgs]]])

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
    [(* line-height x) y]))


(comment
  (prn 'Hi)
  font/fonts
  (font/space-dimention :white 24)
  (js/console.log 
  (j/get (font/get-font :white) :bbox))
  (j/get (font/get-font :white) :capHeight)
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
  (map #(dissoc % :svg) (first (text-component {:width 0 :fill "gray" :color "black" :height 300 :font :white :font-size 24} font/mstr)))
  (text-component {:width 0 :fill "gray" :color "black" :height 300 :font :white :font-size 24} font/mstr)
  (text-runs font/mstr :white 24)
  (font/space-dimention :white 24))
