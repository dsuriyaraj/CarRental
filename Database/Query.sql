to   > 'from'
from < 'to'

--************************************************Clerk*****************************************************************
--Show the vehicle for a specified category that are available in a given location for a given set of dates(usually given as from-date and to-date)
select distinct v.BranchID,v.Vtype_name,v.Vname
from vehicle v
where 1=1
and v.Vlicense not in
(select vlicense from ((select distinct v.Vlicense from vehicle v, RentalAgreement r
where 1=1
and v.Vlicense=r.Vlicense 
and r.Dropoff_time > "" and r.Pickup_time < "" )
union
(select distinct v.Vlicense from vehicle v,Reservation res
where 1=1
and v.Vlicense=res.Vlicense 
and res.Dropoff_time > ""  and res.Pickup_time < "") t1)
and v.Vtype_name like '%%'
and v.BranchID like '%%'
group by v.BranchID,v.Vtype_name,v.Vname

--show the vehicle in a specified location and category that are overdue
select distinct v.BranchID,v.Vtype_name,v.Vname
from RentalAgreement r, vehicle v
where 1=1
and v.Vlicense=r.Vlicense 
and r.Dropoff_time<=(select sysdate() from dual)
and v.Vtype_name like '%%'
and v.BranchID like '%%' 
and not exists
(select 1 from returnvehicle ret
where 1=1
and ret.rentid=r.rentid )
group by v.BranchID,v.Vtype_name,v.Vname

----show the vehicle in a specified location and category that are for sale and their sale prices
select distinct v.BranchID, v.Vtype_name, s.Vlicense, s.Saleprice
from ForsaleVehicles s, vehicle v
where 1=1
and s.vlicense=v.vlicense
and s.SoldFlag=0
and v.Vtype_name like '%%'
and v.BranchID like '%%' 
group by v.BranchID, v.Vtype_name, s.vlicense, s.saleprice


--**********************************************Report*****************************************************************
/*DailyRentals: 
The report contains all the vehicles rented out during the day. 
The entries are grouped by branch, and within each branch, the entries are grouped by vehicle category. 
The report shows the number of vehicles rented from each category, the subtotal for each branch and the grand total for the day.*/

--list of vehicles grouped by branch and vehicle category
select v.BranchID, v.Vtype_name, v.vlicense
from RentalAgreement r, vehicle v
where 1=1
and r.vlicense=v.vlicense
and r.rentedfromDate=curdate()
group by v.BranchID, v.Vtype_name, v.vlicense

--number of vehicles rented by category for each branch
select v.Vtype_name, v.BranchID, count(distinct v.vlicense) 
from RentalAgreement r, vehicle v
where 1=1
and r.vlicense=v.vlicense
and date(r.Pickup_time)=sysdate()
group by v.Vtype_name,v.BranchID

--no of vehicles rented for the day
select count(distinct r.vlicense) 
from RentalAgreement r, vehicle v
where 1=1
and r.vlicense=v.vlicense
and r.rentedfromDate=curdate()
group by r.rentedfromDate

/*Daily Rentals for Branch: This is the same as the Daily Rental report by it is for one specified branch*/




/*Daily Returns: The report contains all the vehicles returned during the day. The entries are grouped by branch, and within each branch, 
the entries are grouped by vehicle category. The report shows the number of vehicles rented from each category 
and the amount of money paid by the customers, the subtotals of the number of vehicles and amount for each branch 
and the grand totals for the day*/

/*Daily Returns for Branch: This is the same as the Daily Returns report, but it is for one specified branch*/
