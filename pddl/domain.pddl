(define (domain snake)
    (:requirements :strips :equality :typing
    )
    (:types TCASELLA TAGENT)
    (:predicates
        (EsParet ?casella - TCASELLA)
        (EstaBuida ?casella - TCASELLA)
        (Veina ?casella ?casella - TCASELLA)
        (EstaAgent ?agent - TAGENT ?casella - TCASELLA)
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
            (EstaAgent ?agent ?aCasella)
            (not (EstaAgent ?agent ?deCasella))
            (not (EstaBuida ?aCasella))
        )
    )
)