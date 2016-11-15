(ns doubleshift.popup
  (:require [khroma.log :as console]
            [khroma.tabs :as tabs]
            [reagent.core :as r]
            [cljs.core.async :as a :refer [>! <! into]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defonce tablist (r/atom []))
(defonce filtered-tabs (r/atom []))

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
          (map #(select-keys % [:url :title :id :favicon]) alltabs)
          (reset! tablist))))))

(defn render-tab [tab]
  (let [{url :url title :title id :id} tab]
    [:li {:key id}
     [:div [:p (trim title)]
           [:a {:href url} (trim url)]]]))

(defn render-tabs []
  (map render-tab @tablist))

(defn filter-tabs [query]
  (console/log query))

(defn render-app []
  [:div
   [:input {:type "text" :placeholder "Search tabs..."
            :on-change #(filter-tabs (-> % .-target .-value))}]
   [:ul (render-tabs)]])

(defn init []
  (get-all-tabs)          ; fetch the data
  (r/render [render-app]  ; render the app
            (js/document.getElementById "app")))
