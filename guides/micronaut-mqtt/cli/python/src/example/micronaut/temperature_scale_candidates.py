from .scale import Scale


class TemperatureScaleCandidates(list):

    def __init__(self):
        super().__init__(list(Scale.candidates()))
