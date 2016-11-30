(ns doubleshift.popup
  (:require [khroma.log :as console]
            [khroma.tabs :as tabs]
            [reagent.core :as r]
            [cljs.core.async :as a :refer [>! <! into]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defonce tablist (r/atom []))
(defonce selected-index (r/atom 0))

(defn trim [s]
  ; TODO: splitting should ideally be done at word
  ; boundaries and not arbitrarily
  (if (< (count s) 70) s
    (str (subs s 0 67) " ...")))

(defn set-active [tab]
  (let [{id :id} tab]
    (tabs/update id {:active true})))

(defn get-all-tabs []
  (let [tabs-chan (tabs/query {:currentWindow true})]
    (go
      (let [alltabs (<! tabs-chan)]
        (->>
          (map #(select-keys % [:url :title :id :favIconUrl]) alltabs)
          (reset! tablist))))))

(defn tab-html
  [idx tab]
  (let [{url :url title :title id :id favicon :favIconUrl} tab]
    (r/create-class
      {:reagent-render
       (fn [idx tab]
         [:li {:class (if (= idx @selected-index) "selected")}
          [:img {:src favicon}]
          [:div
           [:p (trim title)]
           [:a {:href url} (trim url)]]])
       :component-did-mount #(console/log (.-offsetTop (r/dom-node %))) })))

(defn render-tabs []
  [:ul (map-indexed
         (fn [idx tab] ^{:key idx}
           [tab-html idx tab])
         @tablist)])

; filter the tabs based on query
(defn filter-tabs [query]
  (console/log query))

; updates the selected index
; by dx in a cyclic manner
(defn update-index [dx]
  (do (let [n (count @tablist)]
        (reset! selected-index
                (mod (+ @selected-index dx) n)))))

;; render the tab input
(defn tab-input []
   [:input {:type "text" :placeholder "Search tabs..."
            :on-change #(filter-tabs (-> % .-target .-value))
            :on-key-down #(case (.-which %)
                            13 (do (set-active
                                     (nth @tablist @selected-index)))
                            38 (update-index -1)
                            40 (update-index  1)
                            nil)}])

(defn render-app []
  [:div
   (tab-input)
   [render-tabs]])

(defn init []
  (get-all-tabs)          ; fetch the data
  (r/render [render-app]  ; render the app
            (js/document.getElementById "app")))
