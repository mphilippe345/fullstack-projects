import React, { useEffect, useState } from 'react';
import { styled, useTheme } from '@mui/material/styles';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import TextField from '@mui/material/TextField';
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import { FormControl } from '@mui/material';
import Button from '@mui/material/Button';
import InputAdornment from '@mui/material/InputAdornment';
import fetchProductFilters from './FilterMenuService';
import fetchProducts from '../product-page/ProductPageService';

/**
 * @name FilterMenu
 * @description fetches all unique filtering options from API and dsplays them into accordions
 * inside of the filter menu drawer
 * @return component
 */
export default function FilterMenu({
  setFilters, filters, setProducts, setApiError, setAllowTooSpecificError
}) {
  const theme = useTheme();
  const [open, setOpen] = useState(false);
  const [brands, setBrands] = useState([]);
  const [categories, setCategories] = useState([]);
  const [demographics, setDemographics] = useState([]);
  const [colors, setColors] = useState([]);
  const [materials, setMaterials] = useState([]);
  const [minTextInput, setMinTextInput] = useState('');
  const [maxTextInput, setMaxTextInput] = useState('');
  const [minInputErrorText, setMinInputErrorText] = useState('');
  const [maxInputErrorText, setMaxInputErrorText] = useState('');

  useEffect(() => {
    fetchProductFilters(setBrands, setCategories, setDemographics,
      setColors, setMaterials, setApiError);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const replacePound = (string) => {
    const oldString = string;
    const newString = oldString.replace(/#/g, '%23');
    return newString;
  };

  const handleDrawer = () => {
    setOpen(!open);
  };

  const getMinPriceError = (minPrice) => {
    let errorText = '';
    switch (minPrice) {
      case !minPrice.match(/^(([1-9][0-9]{0,14}|0)(\.[0-9]{2}))$|^$/) ? minPrice : '-1':
        errorText = 'Price must be in the format "X.XX".';
        break;
      case parseInt(minPrice, 10) > parseInt(maxTextInput, 10) ? minPrice : '-1':
        errorText = 'Minimum Price cannot exceed Maximum Price.';
        break;
      default: break;
    }
    return errorText;
  };

  const getMaxPriceError = (maxPrice) => {
    let errorText = '';
    switch (maxPrice) {
      case !maxPrice.match(/^(([1-9][0-9]{0,14}|0)(\.[0-9]{2}))$|^$/) ? maxPrice : '-1':
        errorText = 'Price must be in the format "X.XX".';
        break;
      default: break;
    }
    return errorText;
  };

  const onMinInputChange = (event) => {
    if (event.target.value.length < 16) {
      setMinTextInput(event.target.value);
      const filterArray = filters.filter((filter) => !filter.includes('minPrice'));
      if (event.target.value) {
        filterArray.push(`&minPrice=${event.target.value}`);
      }
      setFilters(filterArray);
    }
  };

  const onMaxInputChange = (event) => {
    if (event.target.value.length < 16) {
      setMaxTextInput(event.target.value);
      const filterArray = filters.filter((filter) => !filter.includes('maxPrice'));
      if (event.target.value) {
        filterArray.push(`&maxPrice=${event.target.value}`);
      }
      setFilters(filterArray);
    }
  };

  const handleApplyFiltersClick = () => {
    const minPriceError = getMinPriceError(minTextInput);
    const maxPriceError = getMaxPriceError(maxTextInput);
    setMaxInputErrorText(maxPriceError);
    setMinInputErrorText(minPriceError);

    if (!(minPriceError || maxPriceError)) {
      fetchProducts(setProducts, setApiError, filters);
    }
    setAllowTooSpecificError(true);
  };

  const handleCheck = (event) => {
    const filterArray = [...filters];
    if (event.target.checked) {
      filterArray.push(`&${event.target.id}=${replacePound(event.target.value)}`);
    } else {
      filterArray.splice(filters.indexOf(`&${event.target.id}=${event.target.value}`), 1);
    }
    setFilters(filterArray);
  };

  const DrawerHeader = styled('div')(() => ({
    display: 'flex',
    padding: theme.spacing(0, 1),
    justifyContent: 'flex-start'
  }));

  return (
    <>
      <IconButton
        aria-label="open drawer"
        onClick={handleDrawer}
        edge="start"
        sx={{
          color: filters.length !== 0 ? '#99078c' : '#144012',
          ml: 0.5,
          mr: 0.5,
          mt: 0.25
        }}
      >

        <MenuIcon sx={{ fontSize: '30px' }} />
      </IconButton>

      <Drawer
        PaperProps={{
          sx: {
            backgroundColor: '#f8a1e5',
            width: '240px',
            flexShrink: 0,
            height: '92.5vh',
            marginTop: '72px'
          }
        }}
        variant="persistent"
        anchor="left"
        open={open}
      >
        <DrawerHeader>
          <IconButton onClick={handleDrawer}>
            {theme.direction === 'ltr' ? <ChevronLeftIcon sx={{ color: '#144012' }} /> : <ChevronRightIcon />}
          </IconButton>
        </DrawerHeader>

        <div>
          <Accordion sx={{ backgroundColor: '#f8a1e5', margin: 0 }} key="brands">
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
              key="brandsSummary"
            >
              <Typography key="brandsTypography">Brand</Typography>
            </AccordionSummary>
            <AccordionDetails key="brandsDetails">
              <FormControl key="brandsFormControl">
                <FormGroup key="brandsFormGroup" aria-label="position" column sx={{ fontSize: 10 }}>
                  {brands.map((brand) => (
                    <FormControlLabel
                      control={<Checkbox id="brand" value={brand} color="success" onChange={handleCheck} key={`${brands}Checkbox`} />}
                      label={<Typography variant="body2" key={`${brands}Label`}>{brand}</Typography>}
                      labelPlacement="end"
                      key={`${brand}FormControlLabel`}
                    />
                  ))}
                </FormGroup>
              </FormControl>
            </AccordionDetails>
          </Accordion>

          <Accordion sx={{ backgroundColor: '#f8a1e5', margin: 0 }} key="category">
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
            >
              <Typography key="categoryTypography">Category</Typography>
            </AccordionSummary>
            <AccordionDetails key="categoryDetails">
              <FormControl key="categoryFormControl">
                <FormGroup key="categoryFormGroup" aria-label="position" column sx={{ fontSize: 10 }}>
                  {categories.map((category) => (
                    <FormControlLabel
                      control={<Checkbox id="category" value={category} color="success" onChange={handleCheck} key={`${category}Checkbox`} />}
                      label={<Typography variant="body2" key={`${category}Typography`}>{category}</Typography>}
                      labelPlacement="end"
                      key={`${category}FormControlLabel`}
                    />
                  ))}
                </FormGroup>
              </FormControl>
            </AccordionDetails>
          </Accordion>

          <Accordion sx={{ backgroundColor: '#f8a1e5' }} key="demographic">
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
              key="demographicSummary"
            >
              <Typography key="demographicTypography">Demographic</Typography>
            </AccordionSummary>
            <AccordionDetails key="demographicDetails">
              <FormControl key="demographicFormControl">
                <FormGroup aria-label="position" column sx={{ fontSize: 10 }} key="demographicFormGroup">
                  {demographics.map((demographic) => (
                    <FormControlLabel
                      control={<Checkbox id="demographic" value={demographic} color="success" onChange={handleCheck} key={`${demographic}Checkbox`} />}
                      label={<Typography variant="body2" key={`${demographic}Typography`}>{demographic}</Typography>}
                      labelPlacement="end"
                      key={`${demographic}FormControlLabel`}
                    />
                  ))}
                </FormGroup>
              </FormControl>
            </AccordionDetails>
          </Accordion>

          <Accordion sx={{ backgroundColor: '#f8a1e5' }} key="price">
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
              key="priceSummary"
            >
              <Typography key="priceTypography">Price</Typography>
            </AccordionSummary>
            <AccordionDetails key="priceDetails">
              <List sx={{ boxSizing: 'border-box', height: '250px' }} key="priceList">
                <ListItem key="priceMinListItem">
                  <TextField
                    sx={{ boxSizing: 'border-box', height: '100px' }}
                    label="Minimum Price"
                    className=""
                    id="minPrice"
                    size="small"
                    color="success"
                    value={minTextInput}
                    onChange={onMinInputChange}
                    helperText={minInputErrorText}
                    error={minInputErrorText}
                    InputProps={{
                      startAdornment: <InputAdornment position="start">$</InputAdornment>
                    }}
                    key="priceMinTextField"
                  />
                </ListItem>
                <ListItem key="priceMaxListItem">
                  <TextField
                    label="Maximum Price"
                    className=""
                    id="maxPrice"
                    size="small"
                    color="success"
                    value={maxTextInput}
                    onChange={onMaxInputChange}
                    helperText={maxInputErrorText}
                    error={maxInputErrorText}
                    InputProps={{
                      startAdornment: <InputAdornment position="start">$</InputAdornment>
                    }}
                    key="priceMaxTextField"
                  />
                </ListItem>
              </List>
            </AccordionDetails>
          </Accordion>

          <Accordion sx={{ backgroundColor: '#f8a1e5' }} key="color">
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
            >
              <Typography key="colorTypography">Color</Typography>
            </AccordionSummary>
            <AccordionDetails key="colorDetails">
              <FormControl key="colorFormControl">
                <FormGroup aria-label="position" column sx={{ fontSize: 10 }} key="colorFormGroup">
                  {colors.map((color) => (
                    <FormControlLabel
                      control={<Checkbox id="color" value={color} color="success" onChange={handleCheck} key={`${colors}Checkbox`} />}
                      label={(
                        <Button
                          variant="contained"
                          disabled="true"
                          sx={{
                            backgroundColor: color,
                            height: '10px',
                            '&:disabled': {
                              backgroundColor: color,
                              height: '20px',
                              minWidth: '0px',
                              width: '20px'
                            }
                          }}
                          key={`${color}Label`}
                        />
                      )}
                      labelPlacement="end"
                      key={`${color}FormControlLabel`}
                    />
                  ))}
                </FormGroup>
              </FormControl>
            </AccordionDetails>
          </Accordion>

          <Accordion sx={{ backgroundColor: '#f8a1e5' }} key="material">
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1a-content"
              id="panel1a-header"
            >
              <Typography key="materialTypography">Material</Typography>
            </AccordionSummary>
            <AccordionDetails key="materialDetails">
              <FormControl key="materialFormControl">
                <FormGroup aria-label="position" column sx={{ fontSize: 10 }} key="materialFormGroup">
                  {materials.map((material) => (
                    <FormControlLabel
                      control={<Checkbox id="material" value={material} color="success" onChange={handleCheck} key={`${material}Checkbox`} />}
                      label={<Typography variant="body2" key={`${material}Label`}>{material}</Typography>}
                      labelPlacement="end"
                    />
                  ))}
                </FormGroup>
              </FormControl>
            </AccordionDetails>
          </Accordion>

          <Button
            onClick={handleApplyFiltersClick}
            variant="contained"
            sx={{
              mt: 2, ml: 6, mb: 5, backgroundColor: '#99078c', '&:hover': { backgroundColor: 'purple' }
            }}
          >
            Apply Filters
          </Button>
        </div>
      </Drawer>
    </>
  );
}
