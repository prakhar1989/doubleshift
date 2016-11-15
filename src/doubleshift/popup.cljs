(ns doubleshift.popup
  (:require [khroma.log :as console]
            [khroma.tabs :as tabs]
            [reagent.core :as r]
            [cljs.core.async :as a :refer [>! <! into]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defonce tablist (r/atom []))

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
  (let [{url :url title :title} tab]
    [:div [:p title]]))

(defn filter-tabs [e]
  (console/log "got input"))

(defn render-app []
  [:div
   [:input {:type "text" :placeholder "Search tabs..."
            :on-change filter-tabs}]])

(defn init []
  (get-all-tabs)          ; fetch the data
  (r/render [render-app]  ; render the app
            (js/document.getElementById "app")))
