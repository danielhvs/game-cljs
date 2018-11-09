(ns game-cljs.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [game-cljs.events :as events]
   [game-cljs.views :as views]
   [game-cljs.db :as db]
   [game-cljs.config :as config]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db [(db/cria-snake) (db/cria-maca)]])
  (dev-setup)
  (mount-root))
