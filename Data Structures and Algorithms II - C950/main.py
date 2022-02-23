# Tim Wick - 001482822

import csv
import datetime
from scripts.hash_table import MyHashTable
from scripts.wgups_package import WGUPSPackage
from scripts.delivery_truck import DeliveryTruck


def main():
    """
    Main function to run the program
    :return:
    """
    user_time = input('Enter time (HH:MM) to report status of packages or leave blank to deliver all packages: ')
    if user_time:
        try:
            times = user_time.split(':')
            report_time = datetime.datetime(2022, 1, 1, int(times[0]), int(times[1]), 0)
            wgupsrouting(report_time)
        except ValueError:
            print('Not a valid time, please provide a time in the HH:MM format in 24hr time')
    else:
        wgupsrouting()


def wgupsrouting(report_time=datetime.datetime(2022, 1, 1, 17, 0, 0)):
    """
    Main function
    :param report_time: time to report status of packages
    :return:
    """
    # Creates a hash table of all the packages
    package_hash_table = MyHashTable(initial_capacity=40)
    with open('data/WGUPS Package File.csv') as package_csv:
        reader = csv.reader(package_csv)
        for row in reader:
            package_hash_table.insert(WGUPSPackage(int(row[0]), row[1], row[2] + ' ' + row[3], row[4], row[5], int(row[6]),
                                                   'at the hub', row[7]))

    # Initializes trucks and their assigned delivery zip codes
    truck_1 = DeliveryTruck('Truck1', ['84104', '84115', '84117'])
    truck_2 = DeliveryTruck('Truck2', ['84105', '84103', '84106', '84107', '84111', '84123'])
    truck_3 = DeliveryTruck('Truck3', ['84118', '84119', '84121'])

    # Adds packages to the trucks
    for i in range(package_hash_table.table_len):
        for package in package_hash_table[i]:
            package: WGUPSPackage = package
            if (package.delivery_zip in truck_2.assigned_zips or 'truck 2' in package.special_notes or package.package_id == 14) and package.package_id != 9:
                truck_2.load_package(package)
                package.status = 'on the truck'
            elif package.delivery_zip in truck_3.assigned_zips or 'Delayed' in package.special_notes or 'Wrong' in package.special_notes:
                truck_3.load_package(package)
                package.status = 'on the truck'
            else:
                truck_1.load_package(package)
                package.status = 'on the truck'

    # Builds distance table
    with open('data/WGUPS Distance Table.csv') as distance_csv:
        distance_table = []
        for row in csv.reader(distance_csv):
            distance_table.append(row)

    # Builds routes for each truck
    for truck in [truck_1, truck_2, truck_3]:
        truck.build_route(distance_table)

    # Delivers packages until report time
    truck_1_return = truck_1.deliver_packages(datetime.datetime(2022, 1, 1, 8, 0, 0), report_time, distance_table)
    truck_2.deliver_packages(datetime.datetime(2022, 1, 1, 8, 0, 0), report_time, distance_table)
    truck_3.deliver_packages(truck_1_return, report_time, distance_table)

    # Prints out the package hash table which has the status of all packages and miles each truck drove
    print(package_hash_table)
    for truck in [truck_1, truck_2, truck_3]:
        print(truck.truck_name + ' drove ' + str(truck.distance_driven) + ' miles.')




if __name__ == '__main__':
    main()
