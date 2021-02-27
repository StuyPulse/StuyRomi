class Limelight:
    """ myles """

    def __init__(self, table_name = "limelight"):
        # Network Table Instance
        ntinst = NetworkTablesInstance.getDefault()

        # Limeliht specific network stuff
        self.table = ntinst.getTable(table_name)

        # Standard Limelight Protocol
        self.tx = self.table.getEntry("tx")
        self.ty = self.table.getEntry("ty")

        # TODO: add more standard limelight info

        # Additional Limelight Info (ROMI / OpenCV specific)
        self.min_hue = None

    def handle(self, image):
        pass