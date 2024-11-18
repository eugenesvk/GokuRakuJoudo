; Add various key symbols to be used instead of capitalized letters
(ns karabiner-configurator.keys-symbols
  (:require
   [clojure.string :as string]
   [clojure.core.match :refer [match]]))
(import [java.util.regex Pattern])

; S T O C → left_shift  left_control  left_option  left_command
; R W E Q → right_shift right_control right_option right_command
; SS      → shift ...
; labels must = same position
(def modi-sym      	["⇧"    	 "⎈"      	"⌃"      	 "⎇"     	"⌥"     	 "⌘"      	"◆"      	"❖" ])
(def modi-‹l       	["S"    	 "T"      	"T"      	 "O"     	"O"     	 "C"      	"C"      	"C" ])
(def modi-l›       	["R"    	 "W"      	"W"      	 "E"     	"E"     	 "Q"      	"Q"      	"Q" ])
(def modi-l∀       	["SS"   	 "TT"     	"TT"     	 "OO"    	"OO"    	 "CC"     	"CC"     	"CC" ])
(def modi-l∀-as-key	["shift"	 "control"	"control"	 "option"	"option"	 "command"	"command"	"command" ])
(def ‹key          	["‹" "'"])
(def key›          	["›" "'"])
(def key﹖          	["﹖" "?"])
(def modi-‹l-as-key (mapv #(str "left_"  %) modi-l∀-as-key))
(def modi-l›-as-key (mapv #(str "right_" %) modi-l∀-as-key))

(def keys-symbols-other {
  "🌐" 	"!F","ƒ""!F","ⓕ""!F","Ⓕ""!F","🄵""!F","🅕""!F","🅵""!F"
  "⇪" 	"P"          	; capslock
  "∀" 	"!A"         	; any regardless of side
  "✱" 	"!!"         	; hyper
  "﹖﹖"	"##","??""##"	; optional any
  "︔" 	"semicolon"
  "⸴" 	"comma"
  "．" 	"period"
  "⎋" 	"escape"
  "⭾" 	"tab","↹""tab"
  "₌" 	"equal_sign"
  "⇞" 	"page_up","⇟""page_down"
  "⇤" 	"home","⇥""end","⤒""home","⤓""end","↖""home","↘""end",
  "ˋ" 	"grave_accent_and_tilde","˜""grave_accent_and_tilde"
  "␠" 	"spacebar","␣""spacebar"
  "␈" 	"delete_or_backspace","⌫""delete_or_backspace"
  "␡" 	"delete_forward","⌦""delete_forward"
  "⏎" 	"return_or_enter","↩""return_or_enter","⌤""return_or_enter","␤""return_or_enter",
  "▲" 	"up_arrow","▼""down_arrow","◀""left_arrow","▶""right_arrow"
  " " 	"" ; no-break space removed, used only for rudimentary alignment
  "🔢₁""keypad_1","🔢₂""keypad_2","🔢₃""keypad_3","🔢₄""keypad_4","🔢₅""keypad_5"
  "🔢₆""keypad_6","🔢₇""keypad_7","🔢₈""keypad_8","🔢₉""keypad_9","🔢₀""keypad_0"
  "🔢₌""keypad_equal_sign","🔢₋""keypad_hyphen","🔢₊""keypad_plus"
  "🔢⁄""keypad_slash","🔢．""keypad_period","🔢∗""keypad_asterisk"
})
(def keys-symbols-generated (into {} (
  mapcat      (fn [mod]
    (mapcat   (fn [‹]
      (mapcat (fn [›]
        (map  (fn [﹖]
        (def mI	(.indexOf modi-sym mod)) ; modifier index to pick labels (which have the same position)
        (def ‹l	(nth modi-‹l mI))
        (def l›	(nth modi-l› mI))
        (def l∀	(nth modi-l∀ mI))
        (match [ ; !mandatory ; #optional
          (some? ‹) (some? ›) (some? ﹖)]
          [true      false     false]	{(str ‹ mod    ) 	(str "!" ‹l)}
          [false     true      false]	{(str   mod ›  ) 	(str "!" l›)}
          [false     false     false]	{(str   mod    ) 	(str "!" l∀)}
          [true      false     true] 	{(str ‹ mod   ﹖ )	(str "#" ‹l)}
          [false     true      true] 	{(str   mod › ﹖ )	(str "#" l›)}
          [false     false     true] 	{(str   mod   ﹖ )	(str "#" l∀)}
          :else                      	nil
         )) (concat key﹖ '(nil))
         )) (concat key› '(nil))
         )) (concat ‹key '(nil))
  ))                modi-sym))
)
(def keys-symbols-generated-modi-as-key (into {} (
  mapcat      (fn [mod]
    (mapcat   (fn [‹]
      (map    (fn [›]
        (def mI	(.indexOf modi-sym mod)) ; modifier index to pick labels (which have the same position)
        (def ‹l	(nth modi-‹l-as-key mI))
        (def l›	(nth modi-l›-as-key mI))
        (def l∀	(nth modi-l∀-as-key mI))
        (match [ ; !mandatory ; #optional
          (some? ‹) (some? ›)]
          [true      false ]	{(str ‹ mod    )	‹l}
          [false     true  ]	{(str   mod ›  )	l›}
          [false     false ]	{(str   mod    )	l∀}
          :else             	nil
         )) (concat key› '(nil))
         )) (concat ‹key '(nil))
  ))                modi-sym))
)
(def keys-symbols-other-modi-as-key  {
  "⇪"	"caps_lock"
})

(def keys-symbols-unordered             (merge keys-symbols-generated             keys-symbols-other            ))
(def keys-symbols-unordered-modi-as-key (merge keys-symbols-generated-modi-as-key keys-symbols-other-modi-as-key))
; Sort by key length (BB > A) to match ⇧› before ⇧
(defn sort-map-key-len
  ([m    ] (sort-map-key-len m "asc"))
  ([m ord] (into
  (sorted-map-by (fn [key1 key2] (
    compare
      (if (or (= ord "asc") (= ord "↑")) [(count (str key1)) key1] [(count (str key2)) key2])
      (if (or (= ord "asc") (= ord "↑")) [(count (str key2)) key2] [(count (str key1)) key1])
  ))) m)))
(def keys-symbols             (sort-map-key-len keys-symbols-unordered             "↓"))
(def keys-symbols-modi-as-key (sort-map-key-len keys-symbols-unordered-modi-as-key "↓"))

(defn replace-map-h "input string + hash-map ⇒ string with all map-keys → map-values in input"
  [s_in m_in & {:keys [modi-as-key] :or {modi-as-key nil}}]
  (def keys_in     	(keys m_in))                     	              	;{"⎇""A","⎈""C"} → "⎇""⎈"
  (if modi-as-key  	                                 	              	;
    (def keys_in_q 	(map #(str (Pattern/quote %) "$")	keys_in))     	;"\\Q⎇\\E"   "\\Q⎈\\E" + "$" if passed
    (def keys_in_q 	(map #(     Pattern/quote %)     	keys_in))     	)
  (def keys_in_q_or	(interpose "|"                   	keys_in_q))   	;"\\Q⎇\\E""|""\\Q⎈\\E"
  (def keys_in_q_s 	(apply str                       	keys_in_q_or))	;"\\Q⎇\\E|\\Q⎈\\E"
  (def keys_in_re  	(re-pattern                      	keys_in_q_s)) 	;#"\Q⎇\E|\Q⎈\E"
  (string/replace s_in keys_in_re m_in)
  )

(defn contains-in?
  [m ks]
  (not= ::absent (get-in m ks ::absent)))
(defn update-in-if-has
  [m ks f & args]
  (if (contains-in? m ks)
    (apply (partial update-in m ks f) args)
    m))

(defn key-name-sub-or-self [k]
  (if (keyword? k)
    (do
      (def k1 (replace-map-h k  keys-symbols-modi-as-key :modi-as-key true)) ; first replace modi-as-keys
      (def k2 (replace-map-h k1 keys-symbols)) ; then replace other key symbols
      (keyword (string/replace k2 #"^:" ""))
      )
    (if (map?   k)
      (update-in-if-has k [:key] key-name-sub-or-self)
      k
      )
  )
)

(def modi-re	#"[ACSTOQWERFP]+")
(defn find-modi
  [s prefix matches-found]
  (def modi-prefix-re (re-pattern (str prefix modi-re)))
  (def modi-match (re-find modi-prefix-re s))
  (if (nil? modi-match)
    [s matches-found]
    (do
      (if (vector? modi-match) ; [full-match G1...]
        (def modi-match-str (first modi-match))
        (def modi-match-str        modi-match )
      )
      (def matches-found-cc (conj matches-found modi-match-str))
      (recur (string/replace-first s modi-match-str "") prefix matches-found-cc)
      ))
  )
(defn move-modi-front
  "Replace individual modifiers `!CC!TT` (prefix=!) into a single group with one `!CCTT`"
  [k prefix]
  (def s (if (keyword? k)
    (name k)
          k
  ))
  (def modi-must [])
  (let [[s-remain modi-must] (find-modi s prefix modi-must)]
    (def modi-must-str
      (if (empty? modi-must)
        ""
        (str prefix (string/replace (string/join "" modi-must) prefix "")) ; :!CC!AA → :!CCAA
    ))
    (keyword (str modi-must-str s-remain))
    )
  )
(defn move-modi-mandatory-front
  [k]
  (def prefix "!")
  (move-modi-front k prefix)
  )
(defn move-modi-optional-front
  [k]
  (def prefix "#")
  (move-modi-front k prefix)
  )
(defn move-modi-prefix-front
  [k]
  (move-modi-front (move-modi-front k "#") "!")
  )
(defn key-sym-to-key
  "Takes key with symbols as input and returns keys without; optional :dbg debug print value"
  [k & {:keys [dbg] :or {dbg nil}}]
  (def sub1 (key-name-sub-or-self k))
  (if (not= k sub1)
    (if (map? sub1)
      (def sub (update-in-if-has sub1 [:key] move-modi-prefix-front))
      (def sub (move-modi-prefix-front sub1))
      )
    (def sub                                sub1 )
    )
  ; (if (and (some? dbg) (not= k sub)) (println (str "  " dbg "¦" k " ⟶⟶⟶ " sub)))
  sub
)
