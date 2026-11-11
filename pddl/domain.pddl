(define (domain snake)
    (:requirements :strips :equality :typing :conditional-effects
    )
    (:types TCASELLA TAGENT)
    (:predicates
        (EsParet ?casella - TCASELLA)
        (EstaBuida ?casella - TCASELLA)
        (Veina ?casella ?casella - TCASELLA)
        (EstaAgent ?agent - TAGENT ?casella - TCASELLA)
        (EsSortida ?agent - TAGENT ?casella - TCASELLA)
        (HaSortit ?agent)
    )
(:action Moure
        :parameters (?agent - TAGENT ?deCasella ?aCasella - TCASELLA)
        :precondition (and 
            (EstaBuida ?aCasella)
            (EstaAgent ?agent ?deCasella)
            (or
                (Veina ?aCasella ?deCasella)
                (Veina ?deCasella ?aCasella)
            )
            (not (EsParet ?aCasella))
        )
        :effect (and
            (EsParet ?deCasella)
            (not (EstaAgent ?agent ?deCasella))
            (not (EstaBuida ?aCasella))

            (when (not (EsSortida ?agent ?aCasella))
                (EstaAgent ?agent ?aCasella)
            )

            (when (EsSortida ?agent ?aCasella) 
                (and
                    (HaSortit ?agent)
                    (EsParet ?aCasella)
                )
            )
        )
    )
)