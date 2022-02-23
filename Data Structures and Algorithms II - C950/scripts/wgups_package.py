import datetime


class WGUPSPackage:
    """
    A class for creating a WGUPS package
    """

    def __init__(self, package_id: int, delivery_address: str, delivery_city: str, delivery_zip: str,
                 delivery_deadline: str, package_weight: int, status: str, special_notes: str):
        """
        Initializes a Package object
        :param package_id: id number of the package
        :param delivery_address: delivery address
        :param delivery_city: delivery city
        :param delivery_zip: delivery zip
        :param delivery_deadline: delivery deadline
        :param package_weight: package weight
        :param status: delivery status
        :param special_notes: special notes for the delivery
        """
        self.package_id = package_id
        self.delivery_address = delivery_address
        self.delivery_city = delivery_city
        self.delivery_zip = delivery_zip
        self.delivery_deadline = delivery_deadline
        self.package_weight = package_weight
        self.status = status
        self.special_notes = special_notes
        self.delivered_time = datetime.datetime(1970, 1, 1)

    def __eq__(self, other):
        """Equals method"""
        return self.package_id == self.package_id

    def __hash__(self):
        """Hash method"""
        return hash(self.package_id)

    def __str__(self) -> str:
        """String method to print package information"""
        package_string = 'WGUPS Package ID ' + str(self.package_id) + ' destined for ' + self.delivery_address + ', ' + \
                         self.delivery_city + ', ' + self.delivery_zip + ' at ' + self.delivery_deadline + ' is ' + \
                         self.status + '. Package weighs ' + str(self.package_weight) + 'kg.'
        if self.special_notes:
            package_string += ' Special notes: ' + self.special_notes + '.'
        if self.status == 'delivered':
            package_string += ' Delivered at: ' + str(self.delivered_time.time())
        return package_string

